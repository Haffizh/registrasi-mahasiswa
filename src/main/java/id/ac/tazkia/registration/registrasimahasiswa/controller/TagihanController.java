package id.ac.tazkia.registration.registrasimahasiswa.controller;

import com.sun.org.apache.bcel.internal.generic.NEW;
import id.ac.tazkia.registration.registrasimahasiswa.dao.JenisBiayaDao;
import id.ac.tazkia.registration.registrasimahasiswa.dao.PendaftarDao;
import id.ac.tazkia.registration.registrasimahasiswa.dao.TagihanDao;
import id.ac.tazkia.registration.registrasimahasiswa.dto.TagihanDto;
import id.ac.tazkia.registration.registrasimahasiswa.entity.JenisBiaya;
import id.ac.tazkia.registration.registrasimahasiswa.entity.Pendaftar;
import id.ac.tazkia.registration.registrasimahasiswa.entity.Tagihan;
import id.ac.tazkia.registration.registrasimahasiswa.service.RegistrasiService;
import id.ac.tazkia.registration.registrasimahasiswa.service.TagihanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.HTML;
import javax.validation.Valid;
import java.util.List;


@Controller
public class TagihanController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagihanController.class);


    @Autowired   private TagihanService tagihanService;
    @Autowired   private PendaftarDao pendaftarDao;
    @Autowired   private TagihanDao tagihanDao;
    @Autowired   private JenisBiayaDao jb;

    @ModelAttribute("daftarjenisBiaya")
    public Iterable<JenisBiaya> daftarjenisBiaya(){
        return jb.findAll();
    }


    @GetMapping("/api/pendaftar")
    @ResponseBody
    public Page<Pendaftar> findByName(@RequestParam(required = false) String nomorRegistrasi, Pageable page) {
        if (!StringUtils.hasText(nomorRegistrasi)) {
            return pendaftarDao.findAll(page);
        }
        return pendaftarDao.findByNomorRegistrasiContainingIgnoreCaseOrderByNomorRegistrasi(nomorRegistrasi, page);
    }

    @GetMapping("/api/pendaftar/{pendaftar}/tagihan")
    @ResponseBody
    public List<Tagihan> findByPendaftar(@PathVariable String pendaftar) {
        return tagihanDao.findByPendaftarOrderByTanggalTagihan(pendaftarDao.findOne(pendaftar));
    }

    @RequestMapping("/tagihan/list")
    public ModelMap findByTagihanPendaftar(@RequestParam Pendaftar pendaftar, Pageable page){
        ModelMap mm = new ModelMap();
        mm.addAttribute("daftarTagihan",tagihanDao.findByPendaftarOrderByTanggalTagihan(pendaftar, page));
        mm.addAttribute("pendaftar", pendaftar);
        return mm;
    }


    @RequestMapping("/biaya/tagihan/list")
    public void listTagihan() {
    }


    @RequestMapping(value = "/biaya/tagihan/form")
    public String tampilkanForm(@RequestParam(value = "id", required = false) String id,
                                Model m){
        //defaultnya, isi dengan object baru
        m.addAttribute("tagihan", new Pendaftar());

        if (id != null && !id.isEmpty()){
            Pendaftar p= pendaftarDao.findOne(id);
            if (p != null){
                m.addAttribute("pendaftar", p);
            }
        }
        return "/biaya/tagihan/form";
    }


    @RequestMapping(value = "/biaya/tagihan/form", method = RequestMethod.POST)
    public String prosesForm(@ModelAttribute @Valid TagihanDto tagihanDto, BindingResult errors){

        JenisBiaya jenisBiaya = jb.findOne(tagihanDto.getJenisBiaya().getId());
        if ( jenisBiaya.equals(002)){
            if(errors.hasErrors()){
                return "/biaya/tagihan/form";
            }
            tagihanService.tagihanDu(tagihanDto);
        }
        return "redirect:list";
    }


}
