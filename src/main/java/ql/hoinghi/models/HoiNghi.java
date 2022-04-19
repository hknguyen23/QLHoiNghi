package ql.hoinghi.models;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "HoiNghi")
public class HoiNghi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDHoiNghi")
    private int idHoiNghi;

    public int getIdHoiNghi() {
        return idHoiNghi;
    }

    public void setIdHoiNghi(int idHoiNghi) {
        this.idHoiNghi = idHoiNghi;
    }

    @Column(name = "TenHoiNghi")
    private String tenHoiNghi;

    public String getTenHoiNghi() {
        return tenHoiNghi;
    }

    public void setTenHoiNghi(String tenHoiNghi) {
        this.tenHoiNghi = tenHoiNghi;
    }

    @Column(name = "MoTaNgan")
    private String moTaNgan;

    public String getMoTaNgan() {
        return moTaNgan;
    }

    public void setMoTaNgan(String moTaNgan) {
        this.moTaNgan = moTaNgan;
    }

    @Column(name = "MoTaChiTiet", columnDefinition = "VARCHAR(5000)")
    private String moTaChiTiet;

    public String getMoTaChiTiet() {
        return moTaChiTiet;
    }

    public void setMoTaChiTiet(String moTaChiTiet) {
        this.moTaChiTiet = moTaChiTiet;
    }

    @Lob
    @Column(name = "HinhAnh", columnDefinition = "LONGBLOB")
    private byte[] hinhAnh;

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    @Column(name = "NgayToChuc", columnDefinition = "DATE")
    private Date ngayToChuc;

    public Date getNgayToChuc() {
        return ngayToChuc;
    }

    public void setNgayToChuc(Date ngayToChuc) {
        this.ngayToChuc = ngayToChuc;
    }

    @Column(name = "GioBatDau", columnDefinition = "TIME")
    private Time gioBatDau;

    public void setGioBatDau(Time gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public Time getGioBatDau() {
        return gioBatDau;
    }

    @Column(name = "GioKetThuc", columnDefinition = "TIME")
    private Time gioKetThuc;

    public void setGioKetThuc(Time gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public Time getGioKetThuc() {
        return gioKetThuc;
    }

    @Column(name = "SoNguoiThamDuToiDa")
    private int soNguoiThamDuToiDa;

    public int getSoNguoiThamDuToiDa() {
        return soNguoiThamDuToiDa;
    }

    public void setSoNguoiThamDuToiDa(int soNguoiThamDuToiDa) {
        this.soNguoiThamDuToiDa = soNguoiThamDuToiDa;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDDiaDiem", nullable = false, referencedColumnName = "IDDiaDiem",
            foreignKey = @ForeignKey(name = "FK_HoiNghi_DiaDiem"))
    private DiaDiem diaDiem;

    public DiaDiem getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(DiaDiem diaDiem) {
        this.diaDiem = diaDiem;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hoiNghi", cascade = CascadeType.REMOVE)
    private Set<ChiTietHoiNghi> chiTietHoiNghi;
}
