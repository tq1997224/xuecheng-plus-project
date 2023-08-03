package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO
 */
@Slf4j
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Autowired
    CourseMarketMapper courseMarketMapper;

    @Autowired
    CourseCategoryMapper courseCategoryMapper;

    @Override
    public PageResult<CourseBase> queryCourBaseList(PageParams pageParams, QueryCourseParamsDto courseParamsDto) {

        //详细进行分页查询的单元测试
        //1.拼装擦好像条件
        LambdaQueryWrapper<CourseBase> queryWrapper=new LambdaQueryWrapper<>();
        //根据名称模糊查询,在sql中拼凑courese_base like ‘%值%’
        queryWrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName())
                ,CourseBase::getName
                ,courseParamsDto.getCourseName());
        // 根据课程的审核状态，在sql中擦好像course_base.audit_status=?
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus())
                ,CourseBase::getAuditStatus
                ,courseParamsDto.getAuditStatus());
        //根据课程的发布状态，在Sql中拼凑course_base.Pubstatus=?
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getPublishStatus())
                ,CourseBase::getStatus
                ,courseParamsDto.getPublishStatus());
        //todo 课程发布状态查询未写(已写)

        //创建page分页参数对象
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());

        //开始进行分页查询
        Page<CourseBase> pageresult=courseBaseMapper.selectPage(page,queryWrapper);

        //数据列表
        List<CourseBase> items = pageresult.getRecords();
        //总记录数
        long total = pageresult.getTotal();

        //List<T> items, long counts, long page, long pageSize
        PageResult<CourseBase> courseBasePageResult = new PageResult<CourseBase>(items,total,pageParams.getPageNo(), pageParams.getPageSize());



        return courseBasePageResult;
    }
    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto) {
        //参数合法性校验
//        if (StringUtils.isBlank(dto.getName())) {
////            throw new RuntimeException("课程名称为空");
//            XueChengPlusException.cast("课程名称为空");
//        }
//
//        if (StringUtils.isBlank(dto.getMt())) {
////            throw new RuntimeException("课程分类为空");
//            XueChengPlusException.cast("课程分类为空");
//        }
//
//        if (StringUtils.isBlank(dto.getSt())) {
//            throw new RuntimeException("课程分类为空");
//        }
//
//        if (StringUtils.isBlank(dto.getGrade())) {
//            throw new RuntimeException("课程等级为空");
//        }
//
//        if (StringUtils.isBlank(dto.getTeachmode())) {
//            throw new RuntimeException("教育模式为空");
//        }
//
//        if (StringUtils.isBlank(dto.getUsers())) {
//            throw new RuntimeException("适应人群为空");
//        }
//
//        if (StringUtils.isBlank(dto.getCharge())) {
//            throw new RuntimeException("收费规则为空");
//        }

        //向课程基本信息表course_base写入数据
        CourseBase courseBaseNew = new CourseBase();
        //将传入的页面参数放到BaseName
//        courseBaseNew.setName(dto.getName());
//        courseBaseNew.setDescription(dto.getDescription());
        //上边的从原始对象中get拿数据传入到新对象,比较复杂
        BeanUtils.copyProperties(dto,courseBaseNew); //只要属性名称一致就能拷贝
        courseBaseNew.setCompanyId(companyId);
        courseBaseNew.setCreateDate(LocalDateTime.now());
        //审核状态默认为未提交
        courseBaseNew.setAuditStatus("202002");
        //发布状态未发布
        courseBaseNew.setStatus("203001");
        //插入数据库
        int insert = courseBaseMapper.insert(courseBaseNew);
        if (insert<=0){
            throw new RuntimeException("添加课程失败");
        }
        //向营销表course_market写入数据
        CourseMarket courseMarketNew = new CourseMarket();
        //将页面输入的数据拷贝到新对象中
        BeanUtils.copyProperties(dto,courseMarketNew);
        //主键课程的id
        Long courseid = courseBaseNew.getId();
        courseMarketNew.setId(courseid);
        //保存营销信息
        saveCourseMarket(courseMarketNew);
        //从数据库查询课程的详细信息，包括两部分，
        //向课程营销表course_market写入数据
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(courseid);
        return courseBaseInfo;
    }



    //查询课程信息
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId){
        //从课程基本信息表查询
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase==null){
            return null;
        }
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        //组装到一起
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        if (courseMarket!=null){
            BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);
        }

        //通过courseCategory查询分类信息
        //TODO :课程分类名称设置放入到courseBaseInfoDto
        //获取MT名称
        CourseCategory courseCategoryMt = courseCategoryMapper.selectById(courseBaseInfoDto.getMt());
       courseBaseInfoDto.setMtName(courseCategoryMt.getName());
       //获取st名称
        CourseCategory courseCategorySt = courseCategoryMapper.selectById(courseBaseInfoDto.getSt());
        courseBaseInfoDto.setStName(courseCategorySt.getName());
        return courseBaseInfoDto;

    }

    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto) {
        //数据的合法校验
        Long courseId = editCourseDto.getId();
        //查询课程信息
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        //查询营销基本信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);

        if (courseBase==null){
            XueChengPlusException.cast("课程不存在");
        }

        //根据具体的业务逻辑去校验
        //本机构只能修改本机构的课程
        if (!companyId.equals(courseBase.getCompanyId())){
            XueChengPlusException.cast("课程不属于本机构不能修改");
        }
        //封装数据
        BeanUtils.copyProperties(editCourseDto,courseBase);
        BeanUtils.copyProperties(editCourseDto,courseMarket);
        //修改时间
        courseBase.setChangeDate(LocalDateTime.now());
        //更新数据

        int i = courseBaseMapper.updateById(courseBase);
        if (i<=0){
            XueChengPlusException.cast("修改失败");
        }
        //更新营销信息
        //查询课程信息 TODO 修改
        int update = courseMarketMapper.updateById(courseMarket);
        if (update<=0){
            XueChengPlusException.cast("修改失败");
        }

        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(courseId);
        return courseBaseInfo;
    }

    //单独写一个方法方法保持营销信息 逻辑：存在则更新，不存在则添加
    private int saveCourseMarket(CourseMarket courseMarketNew){
        //参数的合法性校验
        String charge = courseMarketNew.getCharge();
        if (StringUtils.isEmpty(charge)){
            throw new RuntimeException("收费规则为空");
        }
        //如果课程收费，价格没有填写也需要抛出异常
        if (charge.equals("201001")){
            if (courseMarketNew.getPrice()==null||courseMarketNew.getPrice().floatValue()<=0){
//                throw new RuntimeException("课程的价格不能为空且必须大于0");
                XueChengPlusException.cast("课程的价格不能为空且必须大于0");
            }
        }
        //从数据查询营销信息，存在则更新，不存在则添加
        Long id = courseMarketNew.getId(); //主键
        CourseMarket courseMarket = courseMarketMapper.selectById(id);
        if (courseMarket==null){
            //插入数据
            int insert = courseMarketMapper.insert(courseMarketNew);
            return insert;
        }else {
            //将coursemarketNew拷贝到courseMaket
            BeanUtils.copyProperties(courseMarketNew,courseMarket);
            courseMarket.setId(courseMarketNew.getId());
            //更新
            int updateById = courseMarketMapper.updateById(courseMarket);
            return updateById;
        }
    }
}
