package ql.hoinghi.models;

import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(
        name = "NguoiDung",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"IDNguoiDung", "LoaiNguoiDung"},
                name = "UNIQUE_IDNguoiDung_LoaiNguoiDung"
        )
)
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDNguoiDung")
    private int idNguoiDung;

    public int getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(int idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    @Column(name = "TenNguoiDung", columnDefinition = "VARCHAR(50)")
    private String tenNguoiDung;

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    @Column(name = "Username", columnDefinition = "VARCHAR(50)")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "Password", columnDefinition = "VARCHAR(1000)")
    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "Email", columnDefinition = "VARCHAR(100)")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "GioiTinh", columnDefinition = "VARCHAR(3)")
    private String gioiTinh;

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    @Column(name = "IsBanned")
    private boolean isBanned;

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    @Column(name = "LoaiNguoiDung")
    private int loaiNguoiDung;

    public int getLoaiNguoiDung() {
        return loaiNguoiDung;
    }

    public void setLoaiNguoiDung(int loaiNguoiDung) {
        this.loaiNguoiDung = loaiNguoiDung;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "nguoiDung", cascade = CascadeType.REMOVE)
    private Set<ChiTietHoiNghi> chiTietHoiNghi;
}
