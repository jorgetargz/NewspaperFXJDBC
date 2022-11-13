module NewspaperFX {

    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;

    requires lombok;
    requires org.apache.logging.log4j;
    requires io.vavr;
    requires spring.jdbc;

    requires jakarta.inject;
    requires jakarta.annotation;
    requires jakarta.cdi;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires spring.tx;

    exports gui.main;
    exports gui.main.common;
    exports gui.screens.main;
    exports gui.screens.common;
    exports gui.screens.login;
    exports gui.screens.welcome;
    exports gui.screens.newspapers_list;
    exports gui.screens.newspapers_add;
    exports gui.screens.articles_list;
    exports gui.screens.articles_list_with_newspaper;
    exports gui.screens.articles_list_with_bad_rating;
    exports gui.screens.articles_add;
    exports gui.screens.newspapers_delete;
    exports gui.screens.readers_list;
    exports gui.screens.readers_delete;
    exports gui.screens.subscriptions_list;
    exports gui.screens.ratings_list;
    exports gui.screens.ratings_add;
    exports gui.screens.readers_add;
    exports gui.screens.readers_update;
    exports gui.screens.subscriptions_add;
    exports gui.screens.subscriptions_delete;
    exports gui.screens.subscriptions_cancel;


    exports dao;
    exports common;
    exports dao.impl;
    exports dao.impl.row_mapers;
    exports domain.services;
    exports domain.services.impl;
    exports domain.modelo;
    exports configuration;

    opens domain.modelo;
    opens configuration;
    opens gui.main;
    opens gui.screens.common;
    opens gui.screens.login;
    opens gui.screens.main;
    opens gui.screens.welcome;
    opens gui.screens.newspapers_list;
    opens gui.screens.newspapers_add;
    opens gui.screens.newspapers_delete;
    opens gui.screens.articles_add;
    opens gui.screens.articles_list;
    opens gui.screens.articles_list_with_newspaper;
    opens gui.screens.articles_list_with_bad_rating;
    opens gui.screens.readers_list;
    opens gui.screens.readers_delete;
    opens gui.screens.subscriptions_list;
    opens gui.screens.ratings_list;
    opens gui.screens.ratings_add;
    opens gui.screens.readers_add;
    opens gui.screens.readers_update;
    opens gui.screens.subscriptions_add;
    opens gui.screens.subscriptions_delete;
    opens gui.screens.subscriptions_cancel;

    opens css;
    opens fxml;
    opens media;
    opens configs;
    opens common;
    opens dao.utils;
}
