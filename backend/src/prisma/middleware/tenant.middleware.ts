import { Prisma } from '@prisma/client';
import { AsyncLocalStorage } from 'async_hooks';

// This storage will be used to store tenantId per request
export const tenantStorage = new AsyncLocalStorage<{
  tenantId: string;
  role: string;
}>();

export function tenantPrismaMiddleware() {
  return async (
    params: Prisma.MiddlewareParams,
    next: (params: Prisma.MiddlewareParams) => Promise<any>,
  ) => {
    const store = tenantStorage.getStore();
    if (!store) {
      return next(params);
    }
    const { tenantId, role } = store;
    // Only inject tenantId for queries on tenant-scoped models and if not SUPER_ADMIN
    const isTenantScopedModel = [
      'User',
      'Truck',
      'Driver',
      'Trip',
      'FuelRecord',
      'MaintenanceRecord',
      'Expense' /*, 'Truck', 'Driver', ... add more models here */,
    ].includes(params.model ?? '');
    const isSuperAdmin = role === 'SUPER_ADMIN';
    if (isTenantScopedModel && !isSuperAdmin) {
      if (
        [
          'findMany',
          'findFirst',
          'findUnique',
          'update',
          'delete',
          'upsert',
        ].includes(params.action)
      ) {
        if (!params.args) params.args = {};
        if (!params.args.where) params.args.where = {};
        // Only add tenantId filter if not already present and tenantId is a non-empty string
        if (!params.args.where.tenantId && tenantId && tenantId.length > 0) {
          params.args.where.tenantId = tenantId;
        }
      } else if (params.action === 'create') {
        if (!params.args.data) params.args.data = {};
        if (tenantId && tenantId.length > 0) {
          params.args.data.tenantId = tenantId;
        }
      }
    }
    return next(params);
  };
}
