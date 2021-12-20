# DB migrations

Questo progetto e' un programma a riga di comando (Spring boot) per la gestione delle migrazioni di database.

Il progetto usa la libreria [JFMigrate](https://github.com/antonio-fasolato/jfmigrate), istruzioni e guida sul sito.

## Configurazione

La connessione al DB e' nel file `src\main\resources\jfmigrate.properties`, un esempio per SqlServer:

```properties
jfmigrate.db.dialect=sqlserver
jfmigrate.db.url=jdbc:sqlserver://localhost:1433;databaseName=demo
jfmigrate.db.username=username
jfmigrate.db.password=password
jfmigrate.db.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

## Migrazioni

Le migrazioni sono nella cartella ```src\main\java\com\example\demo\migrations\```, seguono lo standard di JFMigrate

## Modalita' di esecuzione

Il programma ha due modalita' di funzionamento: diretto su DB o con output come script sql.

Le opzioni da riga di comando sono:

```
usage: db-migrations
 -b <arg>   Migration starting number (0 = initial state). Useful for the
            script output: can decide from which version to start (number
            passed is included).
 -d         Migrate down
 -e <arg>   Migration ending number (0 = initial state). The program stops
            at this migration regardless of the presence of other ones
            (number passed is excluded).
 -f <arg>   Create migration script in this file
 -h         Help
 -s <arg>   Database schema name (optional)
 -u         Migrate up
 ```

Le due opzioni principali (bisogna specificarne almeno una) sono `-u` o `-d` che producono rispettivamente una migrazione verso l'alto e verso il basso.

`-s` per SqlServer e' inutile.

per `-f` vedere il [paragrafo apposito](#modalita-script)

E' possibile decidere a che migrazione fermarsi, sia in su che in giu', con i due parametri `-b` e `-e`.

### Modalita' diretta

Il programma si collega al database specificato nel file `jfmigrate.properties`, decide in base alla versione del DB quale migrazioni vanno apportate e le esegue (o le reverte, a seconda della modalita').

Se nell'esecuzione avviene un qualche errore, tutte le modifiche sono rollbackate.

### Modalita' script

Passando un nome file al parametro `-f` del programma, viene sempre usato il DB `jfmigrate.properties` come base, ma gli scrip SQL non vengono eseguiti sul DB direttamente ma salvati su file, per essere poi eseguiti in un secondo momento.

Lo script generato, a seconda del motore di database, puo' o meno avere dei controlli sulla versione del database in cui viene eseguito lo script (in modo da non tentare di eseguire due volte la stessa migrazione)

## Intellij launch configuraions

I quattro file qui sotto possono essere scritti nella cartella `.idea\runConfigurations` del proprio IDE e sono 4 esempi di esecuzione del programma, per migrazioni up e down, dirette o su script:

- `Migrations_UP.xml`

    ```xml
    <component name="ProjectRunConfigurationManager">
        <configuration default="false" name="Migrations UP" type="Application" factoryName="Application">
            <option name="MAIN_CLASS_NAME" value="com.my_virtual_space.staffing.DemoApplication" />
            <module name="db-migrations" />
            <option name="PROGRAM_PARAMETERS" value="-u" />
            <method v="2">
            <option name="Make" enabled="true" />
            </method>
        </configuration>
    </component>
    ```
- `Migrations_UP_script.xml`

    ```xml
    <component name="ProjectRunConfigurationManager">
    <configuration default="false" name="Migrations UP script" type="Application" factoryName="Application">
        <option name="MAIN_CLASS_NAME" value="com.my_virtual_space.staffing.DemoApplication" />
        <module name="db-migrations" />
        <option name="PROGRAM_PARAMETERS" value="-u -f &quot;c:\workspaces\demo-migrations\target\UP.sql&quot;" />
        <method v="2">
        <option name="Make" enabled="true" />
        </method>
    </configuration>
    </component>
    ```
- `Migrations_DOWN.xml`

```xml

<component name="ProjectRunConfigurationManager">
  <configuration default="false" name="Migrations DOWN" type="Application" factoryName="Application">
    <option name="MAIN_CLASS_NAME" value="com.my_virtual_space.staffing.DemoApplication"/>
    <module name="db-migrations"/>
    <option name="PROGRAM_PARAMETERS" value="-d"/>
    <method v="2">
      <option name="Make" enabled="true"/>
    </method>
  </configuration>
</component>
```

- `Migrations_DOWN_Script.xml`

```xml

<component name="ProjectRunConfigurationManager">
  <configuration default="false" name="Migrations DOWN Script" type="Application" factoryName="Application">
    <option name="MAIN_CLASS_NAME" value="com.my_virtual_space.staffing.DemoApplication"/>
    <module name="db-migrations"/>
    <option name="PROGRAM_PARAMETERS" value="-d -f &quot;c:\workspaces\demo-migrations\target\DOWN.sql&quot;  -e 8"/>
    <method v="2">
      <option name="Make" enabled="true"/>
    </method>
  </configuration>
</component>
```
