package common;

public class Constantes {
    public static final int SUCCESS = 0;
    public static final int MIN_ID_READER = 0;
    public static final int CONSTRAINT_VIOLATION = -4;
    public static final int SQL_EXCEPTION = -1;
    public static final String ID_READER = "id_reader";
    public static final String ID_NEWSPAPER = "id_newspaper";
    public static final String SIGNING_DATE = "signing_date";
    public static final String CANCELLATION_DATE = "cancellation_date";
    public static final String ID = "id";
    public static final String NAME_READER = "name_reader";
    public static final String BIRTH_READER = "birth_reader";
    public static final int BAD_RATING_LIMIT = 4;
    public static final int MAX_POOL_SIZE = 4;
    public static final int TIMEOUT_MS = 3000;
    public static final int CACHE_SIZE = 250;
    public static final int CACHE_SQL_LIMIT = 2048;
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final int NOT_FOUND_ERROR_CODE = -404;
    public static final int DATABASE_ERROR_CODE = -500;
    public static final int ALREADY_EXISTS_CODE = -409;
    public static final int NOT_FOUND_OR_IN_USE_CODE = -409;
    public static final int ONE_ROW_AFFECTED = 1;
    public static final String THERE_ARE_NO_ARTICLES = "There are no articles";
    public static final String NO_ARTICLES_OF_THIS_NEWSPAPER = "No articles of this newspaper";
    public static final String NO_ARTICLES_OF_THIS_TYPE = "No articles of this type";
    public static final String NO_ARTICLES_OF_THIS_READER = "No articles of this reader";
    public static final String ARTICLE_NOT_FOUND = "Article not found";
    public static final String NO_ARTICLE_TYPES_FOUND = "No article types found";
    public static final String ARTICLE_TYPE_NOT_FOUND = "Article type not found";
    public static final String NO_NEWSPAPERS_FOUND = "No newspapers found";
    public static final String NEWSPAPER_NOT_FOUND = "Newspaper not found";
    public static final String NEWSPAPER_ALREADY_EXISTS = "Newspaper already exists";
    public static final String ARTICLE_TYPE_IN_USE_OR_NOT_FOUND = "Article type in use or not found";
    public static final String ARTICLE_TYPE_ALREADY_EXISTS = "Article type already exists";
    public static final String ARTICLE_ALREADY_EXISTS = "Article already exists";
    public static final String ARTICLE_IN_USE_OR_NOT_FOUND = "Article in use or not found";
    public static final String DATABASE_ERROR = "Database error";
    public static final String UNKNOWN_ERROR = "Unknown error";
    public static final String NO_ARTICLES_AVAILABLE_FOR_READER = "No articles available for reader";
    public static final String THERE_ARE_NO_ARTICLES_OF_THIS_TYPE = "There are no articles of this type";
    public static final String THERE_ARE_NO_ARTICLES_WITH_BAD_RATINGS = "There are no articles of this newspaper with bad ratings";
    private Constantes() {
    }

}
