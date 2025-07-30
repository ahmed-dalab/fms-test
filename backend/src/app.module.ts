import { MiddlewareConsumer, Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { UsersModule } from './users/users.module';
import { AuthModule } from './auth/auth.module';
import { PrismaModule } from './prisma/prisma.module';
import { LoggerMiddleware } from './shared/middleware/logger.middleware';
import { TenantContextMiddleware } from './shared/middleware/tenant-context.middleware';
import { TenantsModule } from './tenants/tenants.module';
import { TrucksModule } from './trucks/trucks.module';
import { DriverModule } from './driver/driver.module';
import { TripsModule } from './trips/trips.module';

@Module({
  imports: [UsersModule, AuthModule, PrismaModule, TenantsModule, TrucksModule, DriverModule, TripsModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {
  configure(consumer: MiddlewareConsumer) {
    consumer
      .apply(TenantContextMiddleware, LoggerMiddleware)
      .forRoutes('*'); // apply to ALL routes
  }
}
