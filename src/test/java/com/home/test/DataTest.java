package com.home.test;

import app.Application;
import model.Book;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.NodeClientFactoryBean;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import service.BookService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class DataTest {

    @Autowired
    private ElasticsearchOperations es;

    @Autowired
    private BookService bookService;

    @BeforeClass
    public static void clientBeanTest() {
        System.out.println("############### BEFORE CLASS");
        NodeClientFactoryBean clientFactoryBean = new NodeClientFactoryBean(true);
        clientFactoryBean.setPathData("target/elasticsearchTestData");
        clientFactoryBean.setPathHome("src/test/resources/test-home-dir");
        clientFactoryBean.setEnableHttp(true);
        clientFactoryBean.setClusterName(UUID.randomUUID().toString());
        try {
            clientFactoryBean.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Before
    public void before() {
        System.out.println("############## BEFORE ");
        es.deleteIndex(Book.class);
        es.createIndex(Book.class);
        es.putMapping(Book.class);
        es.refresh(Book.class);
    }

    @Test
    public void testSave() {

        Book book = new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017");
        Book testBook = bookService.save(book);

        assertNotNull(testBook.getId());
        assertEquals(testBook.getTitle(), book.getTitle());
        assertEquals(testBook.getAuthor(), book.getAuthor());
        assertEquals(testBook.getReleaseDate(), book.getReleaseDate());

    }

    @Test
    public void testFindOne() {

        Book book = new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017");
        bookService.save(book);

        Optional<Book> testBook = bookService.findOne(book.getId());
        if (!testBook.isPresent()) {
            fail();
        }
        Book model = testBook.get();

        assertNotNull(model.getId());
        assertEquals(model.getTitle(), book.getTitle());
        assertEquals(model.getAuthor(), book.getAuthor());
        assertEquals(model.getReleaseDate(), book.getReleaseDate());

    }

//
//    @Test
//    public void testSave() {
//
//        Book book = new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017");
//        Book testBook = bookService.save(book);
//
//        assertNotNull(testBook.getId());
//        assertEquals(testBook.getTitle(), book.getTitle());
//        assertEquals(testBook.getAuthor(), book.getAuthor());
//        assertEquals(testBook.getReleaseDate(), book.getReleaseDate());
//
//    }

    @Test
    public void simpleTest() {
        Map<String, Settings> settingsMap = null;
//            Client client = getNodeClient();
        ClientConfiguration clientConfiguration = ClientConfiguration.create("localhost:9200");
        RestHighLevelClient restHighLevelClient = RestClients.create(clientConfiguration).rest();
        ElasticsearchRestTemplate template = new ElasticsearchRestTemplate(restHighLevelClient);

//            settingsMap = getNodeClient().settings().getAsGroups();
//        template.get("122", Object.class);

    }
}
