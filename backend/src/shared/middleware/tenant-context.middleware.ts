import { Injectable, NestMiddleware, UnauthorizedException } from '@nestjs/common';
import { Request, Response, NextFunction } from 'express';
import { tenantStorage } from 'src/prisma/middleware/tenant.middleware';
import * as jwt from 'jsonwebtoken';

@Injectable()
export class TenantContextMiddleware implements NestMiddleware {
  use(req: Request, res: Response, next: NextFunction) {
    // Extract JWT from Authorization header
    const authHeader = req.headers['authorization'];
    let tenantId: string | undefined;
    let role: string | undefined;
    if (authHeader && authHeader.startsWith('Bearer ')) {
      const token = authHeader.slice(7);
      try {
        // Use the same secret as your JWT strategy
        const payload = jwt.verify(token, 'supersecret') as any;
        tenantId = payload.tenantId;
        role = payload.role;
      } catch (err) {
        throw new UnauthorizedException('Invalid or expired token');
      }
    }
    // Optionally, allow tenantId/role from headers for testing
    if (!tenantId && req.headers['x-tenant-id']) {
      tenantId = req.headers['x-tenant-id'] as string;
    }
    if (!role && req.headers['x-role']) {
      role = req.headers['x-role'] as string;
    }
    // Store in AsyncLocalStorage for this request
    tenantStorage.run({ tenantId: tenantId || '', role: role || '' }, () => {
      next();
    });
  }
} 