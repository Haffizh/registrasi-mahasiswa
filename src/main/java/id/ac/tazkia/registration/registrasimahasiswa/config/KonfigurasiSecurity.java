package id.ac.tazkia.registration.registrasimahasiswa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class KonfigurasiSecurity extends WebSecurityConfigurerAdapter {
    private static final String SQL_LOGIN
            = "select u.username as username,p.password as password, active\n"
            + "FROM s_user u\n"
            + "inner join s_user_password p on p.id_user = u.id\n"
            + "WHERE u.username = ?";

    private static final String SQL_PERMISSION
            = "select u.username, p.permission_value as authority "
            + "from s_user u "
            + "inner join s_role r on u.id_role = r.id "
            + "inner join s_role_permission rp on rp.id_role = r.id "
            + "inner join s_permission p on rp.id_permission = p.id "
            + "where u.username = ?";

    @Autowired
    private DataSource ds;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .jdbcAuthentication()
                .dataSource(ds)
                .usersByUsernameQuery(SQL_LOGIN)
                .authoritiesByUsernameQuery(SQL_PERMISSION)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/sekolah/list").hasAuthority("VIEW_MASTER")
                .antMatchers("/sekolah/form").hasAuthority("VIEW_MASTER")
                .antMatchers("/programstudi/list").hasAuthority("VIEW_MASTER")
                .antMatchers("/periode/list").hasAuthority("VIEW_MASTER")
                .antMatchers("/grade/list").hasAuthority("VIEW_MASTER")
                .antMatchers("/biaya/jenis/form").hasAuthority("VIEW_MASTER")
                .antMatchers("/biaya/nilai/form").hasAuthority("VIEW_MASTER")
                .antMatchers("/registrasi/list").hasAuthority("VIEW_MASTER")
                .antMatchers("/registrasi/detail/list").hasAuthority("VIEW_MASTER")

                .anyRequest().authenticated()
                .and().logout().permitAll()
                .and().formLogin().defaultSuccessUrl("/home")
                .loginPage("/login")
                .permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/registrasi/pendaftar")
                .antMatchers("/api/kokabawal*")
                .antMatchers("/api/sekolah*")
                .antMatchers("/info")
                .antMatchers("/js/*")
                .antMatchers("/img/*")
                .antMatchers("/images/*")
                .antMatchers("/")
                .antMatchers("/css/*");
    }



}

