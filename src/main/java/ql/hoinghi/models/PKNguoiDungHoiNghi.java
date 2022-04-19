package ql.hoinghi.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PKNguoiDungHoiNghi implements Serializable {
    @Column(name = "IDNguoiDung")
    private int nguoiDung;

    @Column(name = "IDHoiNghi")
    private int hoiNghi;
}
