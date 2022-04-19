package ql.hoinghi.DAO;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import ql.hoinghi.App;
import ql.hoinghi.models.DiaDiem;
import ql.hoinghi.models.HoiNghi;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class HoiNghiDAO {
    public HoiNghi getOneById(int id) {
        HoiNghi hoiNghi = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something
            String sql = "SELECT HN FROM HoiNghi AS HN JOIN FETCH HN.diaDiem DD WHERE HN.idHoiNghi = :id";

            hoiNghi = session.createQuery(sql, HoiNghi.class).setParameter("id", id).getSingleResult();

        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return hoiNghi;
    }

    public List<Object[]> getAllHoiNghiWithDiaDiemAndSoLuong() {
        List<Object[]> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something
            String sql = "SELECT HN.IDHoiNghi, HN.TenHoiNghi, HN.NgayToChuc, HN.GioBatDau, HN.GioKetThuc, HN.HinhAnh, " +
                    "HN.MoTaNgan, HN.MoTaChiTiet, HN.SoNguoiThamDuToiDa, DD.IDDiaDiem, DD.TenDiaDiem, DD.DiaChi, DD.SucChua, " +
                    "IFNULL(SoLuong, 0) AS SoLuong " +
                    "FROM HoiNghi HN JOIN DiaDiem DD ON HN.IDDiaDiem = DD.IDDiaDiem LEFT JOIN" +
                    "(SELECT IDHoiNghi, CAST(COUNT(IDNguoiDung) AS CHAR(32)) AS SoLuong " +
                    "FROM ChiTietHoiNghi WHERE TrangThai = 1 GROUP BY IDHoiNghi" +
                    ") CTHN ON CTHN.IDHoiNghi = HN.IDHoiNghi " +
                    "GROUP BY HN.IDHoiNghi ORDER BY HN.IDHoiNghi";

            NativeQuery query = session.createNativeQuery(sql);
            list = query.getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }

    public Object[] getOneHoiNghiWithDiaDiemAndSoLuong(int id) {
        List<Object[]> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something
            String sql = "SELECT HN.IDHoiNghi, HN.TenHoiNghi, HN.NgayToChuc, HN.GioBatDau, HN.GioKetThuc, HN.HinhAnh, " +
                    "HN.MoTaNgan, HN.MoTaChiTiet, HN.SoNguoiThamDuToiDa, DD.IDDiaDiem, DD.TenDiaDiem, DD.DiaChi, DD.SucChua, " +
                    "IFNULL(SoLuong, 0) AS SoLuong " +
                    "FROM HoiNghi HN JOIN DiaDiem DD ON HN.IDDiaDiem = DD.IDDiaDiem LEFT JOIN" +
                    "(SELECT IDHoiNghi, CAST(COUNT(IDNguoiDung) AS CHAR(32)) AS SoLuong " +
                    "FROM ChiTietHoiNghi WHERE TrangThai = 1 GROUP BY IDHoiNghi" +
                    ") CTHN ON CTHN.IDHoiNghi = HN.IDHoiNghi WHERE HN.IDHoiNghi = :id " +
                    "GROUP BY HN.IDHoiNghi ORDER BY HN.IDHoiNghi";

            NativeQuery query = session.createNativeQuery(sql).setParameter("id", id);
            list = query.getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list.get(0);
    }

    public List<Object[]> getAllHoiNghiByIdNguoiDung(int idNguoiDung) {
        List<Object[]> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            String sql = "SELECT HN.TenHoiNghi, HN.NgayToChuc, HN.GioBatDau, HN.GioKetThuc, HN.IDHoiNghi, CTHN.TrangThai " +
                    "FROM HoiNghi HN JOIN ChiTietHoiNghi CTHN ON HN.IDHoiNghi = CTHN.IDHoiNghi " +
                    "WHERE CTHN.IDNguoiDung = :idNguoiDung AND CTHN.TrangThai <> 3 ORDER BY HN.IDHoiNghi";


            NativeQuery query = session.createNativeQuery(sql).setParameter("idNguoiDung", idNguoiDung);
            list = query.getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }

    public List<HoiNghi> getAllHoiNghi() {
        List<HoiNghi> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            String sql = "SELECT HN FROM HoiNghi HN JOIN FETCH HN.diaDiem DD ORDER BY HN.idHoiNghi";

            list = session.createQuery(sql, HoiNghi.class).getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }

    public int getSoNguoiThamDuHoiNghi(int id) {
        int result = 0;

        List<Object[]> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something
            String sql = "SELECT HN.IDHoiNghi, IFNULL(SoLuong, 0) " +
                    "FROM HoiNghi HN LEFT JOIN" +
                    "(SELECT IDHoiNghi, CAST(COUNT(IDNguoiDung) AS CHAR(32)) AS SoLuong " +
                    "FROM ChiTietHoiNghi WHERE TrangThai = 1 GROUP BY IDHoiNghi" +
                    ") CTHN ON HN.IDHoiNghi = CTHN.IDHoiNghi " +
                    "WHERE HN.IDHoiNghi = :id GROUP BY HN.IDHoiNghi";

            list = session.createNativeQuery(sql).setParameter("id", id).getResultList();

            result = list == null ? 0 : Integer.parseInt((String)list.get(0)[1]);

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return result;
    }

    public int getSoNguoiDangChoDuyetHoiNghi(int id) {
        int result = 0;

        List<Object[]> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something
            String sql = "SELECT HN.IDHoiNghi, IFNULL(SoLuong, 0) " +
                    "FROM HoiNghi HN LEFT JOIN" +
                    "(SELECT IDHoiNghi, CAST(COUNT(IDNguoiDung) AS CHAR(32)) AS SoLuong " +
                    "FROM ChiTietHoiNghi WHERE TrangThai = 2 GROUP BY IDHoiNghi" +
                    ") CTHN ON HN.IDHoiNghi = CTHN.IDHoiNghi " +
                    "WHERE HN.IDHoiNghi = :id GROUP BY HN.IDHoiNghi";

            list = session.createNativeQuery(sql).setParameter("id", id).getResultList();

            result = list == null ? 0 : Integer.parseInt((String)list.get(0)[1]);

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return result;
    }

    public int updateHoiNghi(int id, String tenHoiNghi, Date ngayToChuc, Time gioBatDau, Time gioKetThuc, String moTaNgan,
                             String moTaChiTiet, byte[] hinhAnh, int soNguoiThamDuToiDa, DiaDiem diaDiem) {
        int result = 0;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            String sql = "UPDATE HoiNghi SET TenHoiNghi = :tenHoiNghi, NgayToChuc = :ngayToChuc, GioBatDau = :gioBatDau, " +
                    "GioKetThuc = :gioKetThuc, MoTaNgan = :moTaNgan, MoTaChiTiet = :moTaChiTiet, HinhAnh = :hinhAnh, " +
                    "SoNguoiThamDuToiDa = :soNguoiThamDuToiDa, IDDiaDiem = :idDiaDiem WHERE IDHoiNghi = :idHoiNghi";

            NativeQuery query = session.createNativeQuery(sql)
                    .setParameter("tenHoiNghi", tenHoiNghi)
                    .setParameter("ngayToChuc", ngayToChuc)
                    .setParameter("gioBatDau", gioBatDau)
                    .setParameter("gioKetThuc", gioKetThuc)
                    .setParameter("moTaNgan", moTaNgan)
                    .setParameter("moTaChiTiet", moTaChiTiet)
                    .setParameter("hinhAnh", hinhAnh)
                    .setParameter("soNguoiThamDuToiDa", soNguoiThamDuToiDa)
                    .setParameter("idDiaDiem", diaDiem.getIdDiaDiem())
                    .setParameter("idHoiNghi", id);

            result = query.executeUpdate();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return result;
    }

    public void addHoiNghi(String tenHoiNghi, Date ngayToChuc, Time gioBatDau, Time gioKetThuc, String moTaNgan, String moTaChiTiet,
                          byte[] hinhAnh, int soNguoiThamDuToiDa, DiaDiem diaDiem) {
        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            HoiNghi hoiNghi = new HoiNghi();

            hoiNghi.setTenHoiNghi(tenHoiNghi);
            hoiNghi.setNgayToChuc(ngayToChuc);
            hoiNghi.setGioBatDau(gioBatDau);
            hoiNghi.setGioKetThuc(gioKetThuc);
            hoiNghi.setMoTaNgan(moTaNgan);
            hoiNghi.setMoTaChiTiet(moTaChiTiet);
            hoiNghi.setHinhAnh(hinhAnh);
            hoiNghi.setSoNguoiThamDuToiDa(soNguoiThamDuToiDa);
            hoiNghi.setDiaDiem(diaDiem);

            session.persist(hoiNghi);

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();
    }

    public List<String> getTenHoinghiCol() {
        List<String> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something
            String sql = "SELECT TenHoiNghi FROM HoiNghi ORDER BY IDHoiNghi";

            list = session.createNativeQuery(sql).getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }

    public List<HoiNghi> getAllHoiNghiCungThoiDiemToChuc(Date ngayToChuc, Time gioBatDau, Time gioKetThuc, int idDiaDiem) {
        List<HoiNghi> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            // Câu truy vấn này cho phép các hội nghị diễn ra liên tiếp, giờ kết thúc hội nghị này cũng là giờ bắt đầu
            // của hội nghị khác trong cùng 1 địa điểm
            // Ví dụ: 09:30:00 -> 10:00:00, 10:00:00 -> 11:30:00
            String sql = "SELECT HN FROM HoiNghi AS HN JOIN FETCH HN.diaDiem DD " +
                    "WHERE HN.ngayToChuc = :ngayToChuc AND DD.idDiaDiem = :idDiaDiem " +
                    "AND ((:gioBatDau > HN.gioBatDau AND :gioBatDau < HN.gioKetThuc) " +
                    "OR (:gioKetThuc > HN.gioBatDau AND :gioKetThuc < HN.gioKetThuc) " +
                    "OR (:gioBatDau < HN.gioBatDau AND :gioKetThuc > HN.gioKetThuc))";

            list = session.createQuery(sql, HoiNghi.class)
                    .setParameter("ngayToChuc", ngayToChuc)
                    .setParameter("gioBatDau", gioBatDau)
                    .setParameter("gioKetThuc", gioKetThuc)
                    .setParameter("idDiaDiem", idDiaDiem)
                    .getResultList();

        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }

    public List<Object[]> getAllHoiNghiWithSearchValue(String searchValue) {
        List<Object[]> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something
            String sql = "SELECT HN.IDHoiNghi, HN.TenHoiNghi, HN.NgayToChuc, HN.GioBatDau, HN.GioKetThuc, HN.HinhAnh, " +
                    "HN.MoTaNgan, HN.MoTaChiTiet, HN.SoNguoiThamDuToiDa, DD.IDDiaDiem, DD.TenDiaDiem, DD.DiaChi, DD.SucChua, " +
                    "IFNULL(SoLuong, 0) AS SoLuong " +
                    "FROM HoiNghi HN JOIN DiaDiem DD ON HN.IDDiaDiem = DD.IDDiaDiem LEFT JOIN" +
                    "(SELECT IDHoiNghi, CAST(COUNT(IDNguoiDung) AS CHAR(32)) AS SoLuong " +
                    "FROM ChiTietHoiNghi WHERE TrangThai = 1 GROUP BY IDHoiNghi" +
                    ") CTHN ON CTHN.IDHoiNghi = HN.IDHoiNghi " +
                    "WHERE LOWER(HN.TenHoiNghi) LIKE :searchKey OR LOWER(HN.MoTaNgan) LIKE :searchKey " +
                    "GROUP BY HN.IDHoiNghi ORDER BY HN.IDHoiNghi";

            NativeQuery query = session.createNativeQuery(sql)
                    .setParameter("searchKey", "%" + searchValue.toLowerCase() + "%");
            list = query.getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }
}
