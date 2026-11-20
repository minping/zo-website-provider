#namespace("user.login.sql")

    #sql("queryUserByLoginName")
        select *
        from zo_platform_user
        where deleted = '0'
        and login_name = #para(loginName) and password = #para(password)
    #end

    #sql("queryUserByOpenId")
        select *
        from zo_platform_user
        where deleted = '0'
          and open_id = #para(openId)
    #end

#end