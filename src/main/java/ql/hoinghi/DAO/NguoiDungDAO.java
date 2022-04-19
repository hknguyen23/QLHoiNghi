package ql.hoinghi.DAO;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import ql.hoinghi.App;
import ql.hoinghi.models.NguoiDung;
import ql.hoinghi.utils.PasswordUtils;

import java.util.List;

public class NguoiDungDAO {
    public int verifyLogin(String userName, String password) {
        boolean result = false;

        List<Object[]> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            String sql = "SELECT IDNguoiDung, Password FROM NguoiDung WHERE Username = :username";
            list = session.createNativeQuery(sql).setParameter("username", userName).getResultList();

            if (list != null) {
                result = PasswordUtils.verifyHash(password, (String)list.get(0)[1]);
            }

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        if (result) {
            return (int)list.get(0)[0];
        }
        return -1;
    }

    public NguoiDung getOneById(int id) {
        NguoiDung nguoiDung = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            String sql = "FROM NguoiDung ND WHERE IDnguoiDung = :id";
            nguoiDung = session.createQuery(sql, NguoiDung.class)
                    .setParameter("id", id)
                    .getSingleResult();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return nguoiDung;
    }

    public List<NguoiDung> getAllNguoiDung() {
        List<NguoiDung> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            String sql = "FROM NguoiDung";
            list = session.createQuery(sql, NguoiDung.class).getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }

    public void addOne(String name, String userName, String hashPass, String email, String gender) {
        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            NguoiDung nguoiDung = new NguoiDung();
            nguoiDung.setLoaiNguoiDung(2);
            nguoiDung.setTenNguoiDung(name);
            nguoiDung.setUsername(userName);
            nguoiDung.setPassword(hashPass);
            nguoiDung.setEmail(email);
            nguoiDung.setGioiTinh(gender);

            session.persist(nguoiDung);

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();
    }

    public int updateWithoutNewPassword(int id, String name, String email, String gender) {
        int result = 0;
        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            String sql = "UPDATE NguoiDung SET TenNguoiDung = :name, Email = :email, GioiTinh = :gender WHERE IDNguoiDung = :id";

            NativeQuery query = session.createNativeQuery(sql)
                    .setParameter("name", name)
                    .setParameter("email", email)
                    .setParameter("gender", gender)
                    .setParameter("id", id);

            result = query.executeUpdate();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return result;
    }

    public int setBannedStatus(int id, boolean isBanned) {
        int result = 0;
        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            String sql = "UPDATE NguoiDung SET IsBanned = :isBanned WHERE IDNguoiDung = :id";

            NativeQuery query = session.createNativeQuery(sql)
                    .setParameter("isBanned", isBanned)
                    .setParameter("id", id);

            result = query.executeUpdate();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return result;
    }

    public int updatePassword(int id, String hashPass) {
        int result = 0;
        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            String sql = "UPDATE NguoiDung SET Password = :hashPass WHERE IDNguoiDung = :id";

            NativeQuery query = session.createNativeQuery(sql)
                    .setParameter("hashPass", hashPass)
                    .setParameter("id", id);

            result = query.executeUpdate();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return result;
    }

    public List<NguoiDung> getAllNguoiDungNam() {
        List<NguoiDung> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            String sql = "FROM NguoiDung WHERE GioiTinh = 'Nam'";
            list = session.createQuery(sql, NguoiDung.class).getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }

    public List<NguoiDung> getAllNguoiDungNu() {
        List<NguoiDung> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            String sql = "FROM NguoiDung WHERE GioiTinh = 'Nữ'";
            list = session.createQuery(sql, NguoiDung.class).getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }

    public List<NguoiDung> getAllNguoiDungBiCam() {
        List<NguoiDung> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            String sql = "FROM NguoiDung WHERE IsBanned = TRUE";
            list = session.createQuery(sql, NguoiDung.class).getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }

    public List<NguoiDung> getAllNguoiDungKhongBiCam() {
        List<NguoiDung> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            String sql = "FROM NguoiDung WHERE IsBanned = FALSE";
            list = session.createQuery(sql, NguoiDung.class).getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }
}
