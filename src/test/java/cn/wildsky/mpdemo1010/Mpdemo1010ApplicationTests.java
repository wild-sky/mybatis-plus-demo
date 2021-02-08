package cn.wildsky.mpdemo1010;

import cn.wildsky.mpdemo1010.entity.User;
import cn.wildsky.mpdemo1010.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class Mpdemo1010ApplicationTests {
    @Autowired
    private UserMapper userMapper;

    //查询user表中的所有数据
    @Test
    void findAll() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    @Test
    public void addUser() {
        User user = new User();
        user.setName("岳不群");
        user.setAge(88);
        user.setEmail("ttest@qq.com");
        int insert = userMapper.insert(user);
        System.out.println("insert: " + insert);
    }

    //修改
    @Test
    public void updateUser() {
        User user = new User();
        user.setId(2L);
        user.setAge(100);
        int row = userMapper.updateById(user);
        System.out.println(row);

    }
    // 测试乐观锁
    @Test
    public void testOptimisticLocker(){
        User user = userMapper.selectById(1358673070863179778L);
        user.setAge(200);
        userMapper.updateById(user);
    }

    /* id列表查询 */
    @Test
    public void testSelectDemo() {
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 10));
        System.out.println(users);
    }

    /* 条件查询 */
    @Test
    public void testSelectByMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Li Hua");
        map.put("age", 18);
        List<User> users = userMapper.selectByMap(map);
        System.out.println(users);
        users.forEach(System.out::println);
    }
    /* 分页查询 */
    @Test
    public void testPage(){
        /* 1. 创建page对象
         * 传入两个参数
         * @Param:当前页
         * @Param:每页显示的记录数
         * */
        Page<User> page = new Page<>(1,3);
        //调用mp查询方法
        //调用mp分页查询过程中，底层封装

        //把分页所有数据装到page里面
        userMapper.selectPage(page,null);
        //通过page对象获取分页数据
        System.out.println(page.getCurrent());//当前页
        System.out.println(page.getRecords());//每页的list数据
        System.out.println(page.getSize());//每页显示记录数
        System.out.println(page.getTotal());//总记录数
        System.out.println(page.getPages());//总页数
        System.out.println(page.hasNext());//是否有下一页
        System.out.println(page.hasPrevious());//是否有上一页

    }
    /* 删除操作 物理删除 */
    @Test
    public void testDeleteById(){
        int i = userMapper.deleteById(1358716476742299649L);
        System.out.println(i);
    }
    /* 批量删除 物理删除 */
    @Test
    public void testDeleteBatchIds(){
        int i = userMapper.deleteBatchIds(Arrays.asList(2, 3));
        System.out.println(i);
    }
    /** 当配置完逻辑删除条件之后，使用删除操作即为逻辑删除
    * 配置过程：
    * 在数据库添加deleted字段
    * 在entity实体类的删除字段加入注解@TableLogic
    * 在config类加入逻辑删除插件，否则逻辑删除不会生效
     * 一旦配置了逻辑删除插件，则使用mybatis-plus进行sql查询时，会自动排除已被逻辑删除的行
    *  */
    @Test
    public void testLogicDeleteSelect(){
        int result = userMapper.deleteById(1358716476742299649L);
        System.out.println(result);
    }
    /* mp实现复杂查询操作 */
    @Test
    public void testSelectQuery(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //通过QueryWrapper设置条件
        //ge、gt、le、lt、eq、ne
        //>=  >  <=  <   =  !=

        //查询age>=30的记录
        queryWrapper.ge("age",30).eq("id",1358673070863179778L).or().eq("age",60);
        /* 查询指定字段 */
        queryWrapper.select("age");
        /* 查询排序
         * 升序:orderByAsc
         * 降序:orderByDesc */
        queryWrapper.orderByAsc("age");
        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println(users);


    }

}
