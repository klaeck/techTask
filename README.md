<b>DB</b> <br>
PostgreSQL <br> <br>
<b>Data</b> <br>
There are 2 entities: <br>
1. Card <br>
For its purposes following infrastructure was created: CardDataController, CardService, CardRepo, CardRequestDto, and CardResponseDto. <br/>
For testing purposes intended CardIntegrationTest. <br>
2. Category <br>
Its infrastructure is pretty similar to Card's one: CategoryDataController, CategoryService, CategoryRepo, CategoryDto.<br>
CategoryIntegrationTest is a test:)<br>
<br>
<b>Features:</b> <br>
1. Security <br>
Basic security is implemented by SecurityConfig class. <br>
2. Exception mapping <br>
There are few exception mapped: EmptyResultDataAccessException, DataIntegrityViolationException, NotFoundException, and basic handler for any other exceptions.<br>
3. Liquibase migration <br>
I've prepared a migration for DB with Liquibase Maven plugin.
<br> <br> <br>
<h4><b>⚡️A new branch with feature-based structure was created, please take a look</b></h4>
