package jndi.sample.dao;

import jndi.sample.entity.Item;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface ItemDao {
    @Results({
            @Result(property = "id",column = "id",javaType = Long.class),
            @Result(property = "name",column = "name",javaType = String.class)
    })
    @Select("select id,name from items.item where id=#{id}")
    public Item getItemById(@Param("id") Long id);
}
