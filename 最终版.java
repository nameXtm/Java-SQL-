package PrepatedstaementTest;

import org.junit.Test;
import util.JDBCutils;
import util.ORM编程思想;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


//最终版
public class 最终版 {
    public <T> List<T> test1(Class<T> clazz, String sql, Object... argh) throws SQLException, IOException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        Connection connection = JDBCutils.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //填充占位符
        for (int i = 0; i < argh.length; i++) {
            preparedStatement.setObject(i + 1, argh[i]);
        }
            //执行获取结果集
            ResultSet resultSet = preparedStatement.executeQuery();
            //获取结果集的元数据(getMetaData())
            ResultSetMetaData metaData = resultSet.getMetaData();
            //通过ResultSetMetadata获取结果集中的列（getColumnCount）
            int columnCount = metaData.getColumnCount();
            ArrayList<T> list = new ArrayList<>();
            while (resultSet.next()) {
//            ORM编程思想 orm = new ORM编程思想();
               T t = clazz.newInstance();



                //处理结果集一行数据中的每一个列
                for (int c = 0; c < columnCount; c++) {
                    //获取列值（etObject(c + 1)）
                    Object object = resultSet.getObject(c + 1);
                    //获取每个列的列名(getColumnName(c + 1))
                    //获取列的别名 String columnLabel = metaData.getColumnLabel(c + 1);
//                String columnName = metaData.getColumnName(c + 1);
                    String columnLabel = metaData.getColumnLabel(c + 1);
                    //给orm对象指定某个属性赋值,通过反射
                    Field declaredField = clazz.getDeclaredField(columnLabel);
                    declaredField.setAccessible(true);
                    declaredField.set(t, object);

                }
                list.add(t);
        }

           JDBCutils.closeResource(connection, preparedStatement);
            resultSet.close();

        return list;
    }
    @Test
    public void test() throws SQLException, IOException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String sql = "select id ,`name` from test where age > ? ";
        List<ORM编程思想> test1 = test1(ORM编程思想.class, sql, 1);
        test1.forEach(System.out::println);
    }

    }

