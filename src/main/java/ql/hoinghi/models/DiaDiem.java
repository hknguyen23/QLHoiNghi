package ql.hoinghi.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "DiaDiem")
public class DiaDiem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDDiaDiem")
    private int idDiaDiem;

    public int getIdDiaDiem() {
        return idDiaDiem;
    }

    public void setIdDiaDiem(int idDiaDiem) {
        this.idDiaDiem = idDiaDiem;
    }

    @Column(name = "TenDiaDiem")
    private String tenDiaDiem;

    public String getTenDiaDiem() {
        return tenDiaDiem;
    }

    public void setTenDiaDiem(String tenDiaDiem) {
        this.tenDiaDiem = tenDiaDiem;
    }

    @Column(name = "DiaChi", columnDefinition = "VARCHAR(300)")
    private String diaChi;

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    @Column(name = "SucChua")
    private int sucChua;

    public int getSucChua() {
        return sucChua;
    }

    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "diaDiem", cascade = CascadeType.REMOVE)
    private Set<HoiNghi> dsHoiNghi;
}
