package app;

import model.Book;
import org.elasticsearch.action.admin.cluster.settings.ClusterGetSettingsRequest;
import org.elasticsearch.action.admin.cluster.settings.ClusterGetSettingsResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import service.BookService;

import java.io.IOException;

@SpringBootApplication
@ComponentScan(value = {"config", "repository", "service"})
public class Application implements CommandLineRunner {


    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }


    @Autowired
    private ElasticsearchOperations es;

    @Autowired
    private BookService bookService;


    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Override
    public void run(String... args) throws Exception {

//        printElasticSearchInfo();

        bookService.save(new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017"));
        bookService.save(new Book("1002", "Apache Lucene Basics", "Rambabu Posa", "13-MAR-2017"));
        bookService.save(new Book("1003", "Apache Solr Basics", "Rambabu Posa", "21-MAR-2017"));

        //fuzzey search
        Page<Book> books = bookService.findByAuthor("Rambabu", PageRequest.of(0, 10));

        //List<Book> books = bookService.findByTitle("Elasticsearch Basics");

        books.forEach(x -> System.out.println(x));


    }

    //useful for debug, print elastic search details
    private void printElasticSearchInfo() throws IOException {

        System.out.println("--ElasticSearch--");
        ClusterGetSettingsRequest request = new ClusterGetSettingsRequest();
        request.includeDefaults(true);

        ClusterGetSettingsResponse asMap = restHighLevelClient.cluster().getSettings(request, RequestOptions.DEFAULT);

        asMap.getDefaultSettings().getAsGroups().forEach((k, v) -> {
            System.out.println(k + " = " + v);
        });
        System.out.println("--ElasticSearch--");
    }
}
