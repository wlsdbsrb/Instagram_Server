package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from user";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password"))
                );
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from user where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from user where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUserParams);
    }
    public GetUserProfileRes getUserProfile(int userIdx){
        String getUserProfileQuery = "select user.userIdx, " +
                "user.profileImage, " +
                "user.userName, " +
                "user.nickname, " +
                "user.introduce, " +
                "user.email, " +
                "(select count(follow.follower) from follow where follow.follower = ?) as follower, " +
                "(select count(follow.following) from follow where follow.following =?) as following, " +
                "(select count(post.userIdx) from post where post.userIdx = ?) as post " +
                "from user " +
                "left join post on post.userIdx = user.userIdx "+
                "left join follow on follow.follower = user.userIdx and follow.following = user.userIdx "+
                "where user.userIdx=? ORDER BY post.createdAt asc limit 1";

        int getUserProfileParams = userIdx;

        return this.jdbcTemplate.queryForObject(getUserProfileQuery,
                (rs, rowNum) -> new GetUserProfileRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("profileImage"),
                        rs.getString("nickname"),
                        rs.getString("introduce"),
                        rs.getString("email"),
                        rs.getInt("follower"),
                        rs.getInt("following"),
                        rs.getInt("post")
                ),
                getUserProfileParams,getUserProfileParams,getUserProfileParams,getUserProfileParams);
    }
    

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into user (userName, ID, password, email,phone,nickname,birth) VALUES (?,?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getUserName(), postUserReq.getID(), postUserReq.getPassword(), postUserReq.getEmail()
                ,postUserReq.getPhone(),postUserReq.getNickname(),postUserReq.getBirth()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from user where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int modifyUserInfo(PatchUserReq patchUserReq){
        String modifyUserInfoQuery = "update user set userName=?, nickname=?, introduce=?, email= ? where userIdx = ? ";
        Object[] modifyUserInfoParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getNickname(),
                patchUserReq.getIntroduce(), patchUserReq.getEmail(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserInfoQuery,modifyUserInfoParams);
    }
    public int modifyProfileImage(PatchProfileImageReq patchProfileImageReq){
        String modifyProfileImageQuery = "update user set profileImage=? where userIdx = ?";
        Object[] modifyProfileImageParams = new Object[]{patchProfileImageReq.getProfileImage(), patchProfileImageReq.getUserIdx()};

        return  this.jdbcTemplate.update(modifyProfileImageQuery,modifyProfileImageParams);
    }
    public int modifyPassword(PatchPasswordReq patchPasswordReq){
        String modifyPasswordQuery = "update user set password=? where userIdx = ?";
        Object[] modifyPasswordParams = new Object[]{patchPasswordReq.getPassword(), patchPasswordReq.getUserIdx()};

        return  this.jdbcTemplate.update(modifyPasswordQuery,modifyPasswordParams);
    }


    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, password,email,userName,ID,nickname,profileImage from user where ID = ?";
        String getPwdParams = postLoginReq.getID();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("ID"),
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("nickname"),
                        rs.getString("profileImage")
                ),
                getPwdParams
                );

    }



}
