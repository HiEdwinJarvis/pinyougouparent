package com.jarvis.pinyougou.shop.service;

import com.jarvis.pinyougou.pojo.TbSeller;
import com.jarvis.pinyougou.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:认证类
 * @CreateDate: 2019/6/13 20:53
 * @UpdateUser: jarvis
 * @UpdateDate: 2019/6/13 20:53
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class UserDetailsServiceImpl implements UserDetailsService {



    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("经过了UserDetailsServiceImpl");
        List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));//授权
        TbSeller seller = sellerService.findOne(username);
        if(seller!=null){
            //数据库中存在而且审核通过的
            if(seller.getStatus().equals("1")){
                return new User(username,seller.getPassword(), grantedAuths);
            }else {
                return null;
            }
        }else{
            return null;
        }





    }
}
