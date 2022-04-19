package ql.hoinghi.DAO;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import ql.hoinghi.App;
import ql.hoinghi.models.DiaDiem;

import java.util.List;

public class DiaDiemDAO {
    public DiaDiem getOneById(int id) {
        DiaDiem diaDiem = new DiaDiem();

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something
            diaDiem = session.get(DiaDiem.class, id);

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return diaDiem;
    }

    public List<String> getTenDiaDiemCol() {
        List<String> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something
            String sql = "SELECT TenDiaDiem FROM DiaDiem ORDER BY IDDiaDiem";

            list = session.createNativeQuery(sql).getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }

    public List<DiaDiem> getAllDiaDiem() {
        List<DiaDiem> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something
            String sql = "FROM DiaDiem ORDER BY IDDiaDiem";

            list = session.createQuery(sql, DiaDiem.class).getResultList();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return list;
    }

    public int getMinSucChuaCoTheSuaDuoc(int id) {
        int result = 0;

        List<Object[]> list = null;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something
            String sql = "SELECT DD.IDDiaDiem, MAX(HN.SoNguoiThamDuToiDa) " +
                    "FROM DiaDiem DD JOIN HoiNghi HN ON DD.IDDiaDiem = HN.IDDiaDiem " +
                    "WHERE DD.IDDiaDiem = :id GROUP BY DD.IDDiaDiem";

            list = session.createNativeQuery(sql).setParameter("id", id).getResultList();

            result = list == null ? 0 : (int)list.get(0)[1];

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return result;
    }

    public int addDiaDiem(String tenDiaDiem, String diaChi, int sucChua) {
        int result = 0;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something

            DiaDiem diaDiem = new DiaDiem();
            diaDiem.setTenDiaDiem(tenDiaDiem);
            diaDiem.setDiaChi(diaChi);
            diaDiem.setSucChua(sucChua);

            session.persist(diaDiem);

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return result;
    }

    public int updateDiaDiem(int id, String tenDiaDiem, String diaChi, int sucChua) {
        int result = 0;

        Session session = App.sessionFactory.getCurrentSession();

        try {
            session.getTransaction().begin();
            // do something
            String sql = "UPDATE DiaDiem SET TenDiaDiem = :tenDiaDiem, DiaChi = :diaChi, SucChua = :sucChua WHERE IDDiaDiem = :id";

            NativeQuery query = session.createNativeQuery(sql)
                    .setParameter("tenDiaDiem", tenDiaDiem)
                    .setParameter("diaChi", diaChi)
                    .setParameter("sucChua", sucChua)
                    .setParameter("id", id);

            result = query.executeUpdate();

        } catch(Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }

        session.close();

        return result;
    }
}
