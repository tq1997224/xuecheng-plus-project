package com.xuecheng.content.service.impl;

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: Tq
 * @Desc:   TODO 课程分类接口的实现
 * @create: 2023-07-31 20:33
 **/
@Slf4j
@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Autowired
    CourseCategoryMapper courseCategoryMapper;
    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        //调用Mapper递归查询分类信息
       List<CourseCategoryTreeDto> courseCategoryTreeDtos= courseCategoryMapper.selectTreeNodes(id);

        //找到每个节点的子节点，封装成List《courseCategoryMapper》
        //先将list转成map，key就是节点的id，value就是courseCategoryTreeDto对象
        Map<String, CourseCategoryTreeDto> mapTemp = courseCategoryTreeDtos.stream()
                .filter(item->!id.equals(item.getId()))
                .collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));
        //定义list，作为最终返回的值
        ArrayList<CourseCategoryTreeDto> categoryTreeDtoArrayList = new ArrayList<>();
        //从头遍历，一边遍历，一边找子节点
        courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId()))
                .forEach(item->{
                if(item.getParentid().equals(id)){
                    categoryTreeDtoArrayList.add(item);
                }
                //找到每个子节点放到父节点中childrenTreeNodes属性中
                    //找到父节点courseCategoryTreeDto
                           CourseCategoryTreeDto courseCategoryTreeDto = mapTemp.get(item.getParentid());
                if (courseCategoryTreeDto!=null) {
                    if (courseCategoryTreeDto.getChildrenTreeNodes() == null) {
                        //父节点的字节点属性为空，则需要new一个集合放入子节点
                        courseCategoryTreeDto.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());

                    }
                    //到每个节点间的子子节点放入ChildrenTreeNodes属性中
                    courseCategoryTreeDto.getChildrenTreeNodes().add(item);
                }

                });

        return categoryTreeDtoArrayList;
    }
}
