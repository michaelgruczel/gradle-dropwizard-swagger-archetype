package de.mgruc.service;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerDropwizard;

import org.skife.jdbi.v2.DBI;

import de.mgruc.service.auth.ServiceAuthenticator;
import de.mgruc.service.auth.User;
import de.mgruc.service.db.ServiceDAO;
import de.mgruc.service.resources.EntryResource;

public class BackendService extends Application<BackendServiceConfiguration> {

	private final SwaggerDropwizard swaggerDropwizard = new SwaggerDropwizard();

	public static void main(String[] args) throws Exception {
		new BackendService().run(args);
	}

	@Override
	public void run(BackendServiceConfiguration configuration,
			Environment environment) throws Exception {

		final DBIFactory factory = new DBIFactory();
		final DBI jdbi = factory.build(environment,
				configuration.getDataSourceFactory(), "example");

		final ServiceDAO dao = jdbi.onDemand(ServiceDAO.class);

		environment.jersey()
				.register(
						new BasicAuthProvider<User>(new ServiceAuthenticator(),
								"admin"));
		environment.jersey().register(new EntryResource(dao));
		swaggerDropwizard.onRun(configuration, environment);
		dao.createEntryTable();
	}

	@Override
	public void initialize(Bootstrap<BackendServiceConfiguration> bootstrap) {
		bootstrap
				.addBundle(new MigrationsBundle<BackendServiceConfiguration>() {
					@Override
					public DataSourceFactory getDataSourceFactory(
							BackendServiceConfiguration configuration) {
						return configuration.getDataSourceFactory();
					}
				});

		bootstrap.addBundle(new AssetsBundle());
		swaggerDropwizard.onInitialize(bootstrap);

	}

}
