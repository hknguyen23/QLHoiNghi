package ql.hoinghi.models;

import javax.persistence.*;

@Entity
@IdClass(PKNguoiDungHoiNghi.class)
@Table(name = "ChiTietHoiNghi")
public class ChiTietHoiNghi {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDNguoiDung", referencedColumnName = "IDNguoiDung",
            foreignKey = @ForeignKey(name = "FK_ChiTietHoiNghi_NguoiDung"))
    private NguoiDung nguoiDung;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDHoiNghi", referencedColumnName = "IDHoiNghi",
            foreignKey = @ForeignKey(name = "FK_ChiTietHoiNghi_HoiNghi"))
    private HoiNghi hoiNghi;

    @Column(name = "TrangThai")
    private int trangThai;

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
}
