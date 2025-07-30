import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { LoggingInterceptor } from './shared/interceptors/logging.interceptor';
import { ValidationPipe } from '@nestjs/common';
import { TenantContextMiddleware } from './shared/middleware/tenant-context.middleware';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.enableCors({
    origin: '*', // Allow all origins (for development only)
    credentials: true,
  }); // ✅ Allow all origins
  // 👇 Apply tenant context middleware globally
  app.use(new TenantContextMiddleware().use); // 👈 this is crucial

  // set every route /api
  app.setGlobalPrefix('api');
  // 👇 Pipes act like a *gate scanner*
  app.useGlobalPipes(new ValidationPipe({ whitelist: true }));

  // Order matters: logging first, then response wrapper
  app.useGlobalInterceptors(new LoggingInterceptor());
  await app.listen(process.env.PORT ?? 3000, '0.0.0.0');
}
bootstrap();
