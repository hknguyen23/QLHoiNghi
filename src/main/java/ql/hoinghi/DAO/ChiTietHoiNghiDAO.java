package ql.hoinghi.DAO;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import ql.hoinghi.App;

import java.util.List;

public class ChiTietHoiNghiDAO {
    public List<Object[]> getAllHoinghiByIDNguoiDung(int idNguoiDung) {
        List<Object[]> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something
            String sql = "SELECT IDHoiNghi, TrangThai FROM ChiTietHoiNghi WHERE IDNguoiDung = :id";

            NativeQuery query = session.createNativeQuery(sql).setParameter("id", idNguoiDung);
            list = query.getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }

    public int huyDangKy(int idHoiNghi, int idNguoiDung) {
        int result = 0;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();

            // do something
            String sql = "DELETE FROM ChiTietHoiNghi WHERE IDHoiNghi = :idHoiNghi AND IDNguoiDung = :idNguoiDung";

            NativeQuery query = session.createNativeQuery(sql)
                    .setParameter("idNguoiDung", idNguoiDung)
                    .setParameter("idHoiNghi", idHoiNghi);

            result = query.executeUpdate();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return result;
    }

    public int dangKy(int idHoiNghi, int idNguoiDung) {
        int result = 0;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            // Trạng thái 2: đăng ký tham dự và chờ admin duyệt
            String sql = "INSERT INTO ChiTietHoiNghi(IDHoiNghi, IDNguoiDung, TrangThai) VALUES(:idHoiNghi, :idNguoiDung, 2)";

            NativeQuery query = session.createNativeQuery(sql)
                    .setParameter("idHoiNghi", idHoiNghi)
                    .setParameter("idNguoiDung", idNguoiDung);

            result = query.executeUpdate();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return result;
    }

    // Admin duyệt hoặc cấm người dùng
    public int capNhatTrangThai(int idHoiNghi, int idNguoiDung, int trangThai) {
        int result = 0;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();

            // do something

            String sql = "UPDATE ChiTietHoiNghi SET TrangThai = :trangThai WHERE IDHoiNghi = :idHoiNghi AND IDNguoiDung = :idNguoiDung";

            NativeQuery query = session.createNativeQuery(sql)
                    .setParameter("trangThai", trangThai)
                    .setParameter("idHoiNghi", idHoiNghi)
                    .setParameter("idNguoiDung", idNguoiDung);

            result = query.executeUpdate();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return result;
    }

    public List<Object[]> getAllNguoiDungChoDuyetByIDHoiNghi(int idHoiNghi) {
        List<Object[]> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something
            String sql = "SELECT ND.IDNguoiDung, ND.TenNguoiDung, ND.Username, ND.Email, CTHN.TrangThai " +
                    "FROM NguoiDung ND JOIN ChiTietHoiNghi CTHN ON ND.IDNguoiDung = CTHN.IDNguoiDung " +
                    "WHERE CTHN.IDHoiNghi = :id AND CTHN.TrangThai <> 1";

            NativeQuery query = session.createNativeQuery(sql).setParameter("id", idHoiNghi);
            list = query.getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }

    public List<Object[]> getAllNguoiDungDaThamDuByIDHoiNghi(int idHoiNghi) {
        List<Object[]> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something
            String sql = "SELECT ND.IDNguoiDung, ND.TenNguoiDung, ND.Username, ND.Email, ND.GioiTinh " +
                    "FROM NguoiDung ND JOIN ChiTietHoiNghi CTHN ON ND.IDNguoiDung = CTHN.IDNguoiDung " +
                    "WHERE CTHN.IDHoiNghi = :id AND CTHN.TrangThai = 1";

            NativeQuery query = session.createNativeQuery(sql).setParameter("id", idHoiNghi);
            list = query.getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }
}
