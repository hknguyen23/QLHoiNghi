module ql.hoinghi {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.persistence;
    requires java.naming;
    requires net.bytebuddy;
    requires java.xml.bind;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;
    requires jbcrypt;
    requires slf4j.api;
    requires java.desktop;
    requires javafx.swing;

    opens ql.hoinghi to javafx.fxml;
    opens ql.hoinghi.controllers to javafx.fxml;
    opens ql.hoinghi.models to org.hibernate.orm.core, javafx.base;

    exports ql.hoinghi;
    exports ql.hoinghi.controllers;
}