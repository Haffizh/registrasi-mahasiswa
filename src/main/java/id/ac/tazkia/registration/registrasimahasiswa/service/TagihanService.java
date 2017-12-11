package id.ac.tazkia.registration.registrasimahasiswa.service;

import id.ac.tazkia.registration.registrasimahasiswa.dao.NilaiBiayaDao;
import id.ac.tazkia.registration.registrasimahasiswa.dao.TagihanDao;
import id.ac.tazkia.registration.registrasimahasiswa.dto.TagihanDto;
import id.ac.tazkia.registration.registrasimahasiswa.entity.JenisBiaya;
import id.ac.tazkia.registration.registrasimahasiswa.entity.NilaiBiaya;
import id.ac.tazkia.registration.registrasimahasiswa.entity.Pendaftar;
import id.ac.tazkia.registration.registrasimahasiswa.entity.Tagihan;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service @Transactional
public class TagihanService {
    private static final String JENIS_BIAYA_PENDAFTARAN = "001";
    private static final String JENIS_BIAYA_DU = "002";

    @Autowired private NilaiBiayaDao nilaiBiayaDao;
    @Autowired private TagihanDao tagihanDao;
    @Autowired private  TagihanService tagihanService;

    private JenisBiaya pendaftaran;
    private JenisBiaya du;

    public TagihanService(){
        pendaftaran = new JenisBiaya();
        pendaftaran.setId(JENIS_BIAYA_PENDAFTARAN);
        du = new JenisBiaya();
        du.setId(JENIS_BIAYA_DU);
    }



    public Tagihan createTagihanPendaftaran(Pendaftar p){
        Tagihan t = new Tagihan();
        t.setPendaftar(p);
        t.setTanggalTagihan(LocalDate.now());
        t.setLunas(false);

        BigDecimal kodeUnik = new BigDecimal(p.getNomorRegistrasi().substring(p.getNomorRegistrasi().length() - 3));
        BigDecimal biayaPendaftaran = hitungBiayaPendaftaran();
        t.setNilai(biayaPendaftaran.add(kodeUnik));
        t.setKeterangan("Biaya Pendaftaran Mahasiswa Tazkia");
        t.setNomorTagihan("PMB-"+p.getNomorRegistrasi());
        t.setJenisBiaya(pendaftaran);

        tagihanDao.save(t);
        return t;
    }


    public Tagihan tagihanDu( TagihanDto tagihanDto){
        Tagihan t = new Tagihan();
        Pendaftar p = new Pendaftar();
        JenisBiaya jenisBiaya = new JenisBiaya();
        t.setJenisBiaya(jenisBiaya);
        t.setPendaftar(p);
        t.setTanggalTagihan(LocalDate.now());
        t.setLunas(false);

        BeanUtils.copyProperties(tagihanDto, t);
        t.setNomorTagihan("DU-"+(p.getNomorRegistrasi()));

        tagihanDao.save(t);
        return t;
    }



    public BigDecimal hitungBiayaPendaftaran(){
        Page<NilaiBiaya> biaya = nilaiBiayaDao.findByJenisBiaya(pendaftaran, new PageRequest(0,1));
        if(!biaya.hasContent()){
            return BigDecimal.ZERO;
        }
        return biaya.getContent().get(0).getNilai();
    }
}
