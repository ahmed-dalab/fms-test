import { Injectable, OnModuleDestroy, OnModuleInit } from '@nestjs/common';
import { PrismaClient } from '@prisma/client';
import { tenantPrismaMiddleware } from './middleware/tenant.middleware';

@Injectable()
export class PrismaService
  extends PrismaClient
  implements OnModuleInit, OnModuleDestroy
{
  constructor() {
    super();
    this.$use(tenantPrismaMiddleware());
  }
  async onModuleInit() {
    await this.$connect();
  }

  // This method will be called when the application is shutting down
  async onModuleDestroy() {
    await this.$disconnect();
  }
}
