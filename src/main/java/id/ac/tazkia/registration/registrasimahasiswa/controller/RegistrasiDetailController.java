package id.ac.tazkia.registration.registrasimahasiswa.controller;

import id.ac.tazkia.registration.registrasimahasiswa.dao.DetailPendaftarDao;
import id.ac.tazkia.registration.registrasimahasiswa.dao.PendaftarDao;
import id.ac.tazkia.registration.registrasimahasiswa.dao.UserDao;
import id.ac.tazkia.registration.registrasimahasiswa.dto.RegistrasiDetail;
import id.ac.tazkia.registration.registrasimahasiswa.entity.DetailPendaftar;
import id.ac.tazkia.registration.registrasimahasiswa.entity.Pendaftar;
import id.ac.tazkia.registration.registrasimahasiswa.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrasiDetailController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrasiDetailController.class);

    @Autowired private PendaftarDao pendaftarDao;
    @Autowired private UserDao userDao;

    @Autowired
    private DetailPendaftarDao detailPendaftarDao;

    @GetMapping("/registrasi/detail/list")
    public void daftarPendaftaran(Model m){
        m.addAttribute("daftarPendaftaranakhir", detailPendaftarDao.findAll());
    }


    @GetMapping(value = "/registrasi/detail/form")
    public String tampilkanForm(Model model, Authentication currentUser){
        logger.debug("Authentication class : {}",currentUser.getClass().getName());

        if(currentUser == null){
            return "/registrasi/detail/form";
        }

        User u = userDao.findByUsername(((UserDetails)currentUser.getPrincipal()).getUsername());
        logger.debug("User ID : {}", u.getId());
        if(u == null){
            return "/registrasi/detail/form";
        }

        Pendaftar p = pendaftarDao.findByUser(u);
        logger.debug("Nama Pendaftar : "+p.getNama());
        if(p == null){
            return "/registrasi/detail/form";
        }

        RegistrasiDetail detail = new RegistrasiDetail();
        detail.setNama(p.getNama());
        detail.setPendaftar(p.getId());
        detail.setEmail(p.getEmail());
        detail.setAsalSekolah(p.getNamaAsalSekolah());
        detail.setKabupatenKota(p.getKabupatenKota().getNama());
        model.addAttribute("registrasi", detail);


        DetailPendaftar d = detailPendaftarDao.findByPendaftar(p);
        logger.debug("Nomor Registrasi :"+ p.getNomorRegistrasi());
        if (d == null){
            return "/registrasi/detail/form";
        }

        model.addAttribute("detail", detailPendaftarDao.findByPendaftar(p));
        return "/registrasi/detail/form";

    }

    @PostMapping(value = "/registrasi/detail/form")
    public String prosesForm(@ModelAttribute @Valid DetailPendaftar p, BindingResult errors){
        if(errors.hasErrors()){
            logger.debug("Error Validasi Form : {}", errors.toString());
            return "/registrasi/detail/form";
        }
        detailPendaftarDao.save(p);
        return "redirect:/home";

    }


}
