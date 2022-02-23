package top.lcywings.pony.model.common;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
@Data
@Accessors(chain = true)
public class DropDownModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 层级
     */
    private Integer treeLevel;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 子集合
     */
    private List<DropDownModel> sonList;

    /**
     * 构建树形结构
     *
     * @param list     查询出来的数据
     * @param parentId 父节点的id
     * @return 构建出来的结果
     * @author yeweiwei
     * @date 2021/11/24 19:39
     */
    public static List<DropDownModel> buildTree(List<DropDownModel> list, Long parentId) {
        //筛选出根节点
        List<DropDownModel> dropList =
                list.stream().filter(s -> Objects.equals(s.getParentId(), parentId)).collect(Collectors.toList());

        //循环查询子节点
        for (DropDownModel dropDownModel : dropList) {
            List<DropDownModel> sonList = buildTree(list, dropDownModel.getId());
            if (!CollectionUtils.isEmpty(sonList)){
                dropDownModel.setSonList(sonList);
            }
        }
        return dropList;
    }
}
