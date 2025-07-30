import { Injectable } from '@nestjs/common';
import { PrismaService } from 'src/prisma/prisma.service';
import { TenantListResponseDto } from './dto/tenant-response.dto';
import {
  UpdateTenantDto,
  UpdateTenantSettingDto,
} from './dto/update-tenant.dto';

@Injectable()
export class TenantsRepository {
  constructor(private prismaService: PrismaService) {}
  // Define methods for interacting with the database, e.g., createTenant, findTenantById, etc.
  // This is a placeholder for the actual implementation.
  async createTenant(data: any) {
    return this.prismaService.tenant.create({
      data: {
        name: data.name,
        email: data.email,
        phoneNumber: data.phoneNumber,
      },
    });
  }
  // Example method to find a tenant by ID
  async findTenantById(id: string) {
    return this.prismaService.tenant.findUnique({
      where: { id },
      include: {
        setting: true,
      },
    });
  }
  async getAllTenants(
    where: any,
    skip: number,
    limit: number,
    page: number,
  ): Promise<TenantListResponseDto> {
    const [data, total] = await this.prismaService.$transaction([
      this.prismaService.tenant.findMany({
        where,
        skip,
        take: limit,
        orderBy: { createdAt: 'desc' },
        include: {
          setting: true,
        },
      }),
      this.prismaService.tenant.count({ where }),
    ]);

    return {
      data,
      meta: {
        total,
        page,
        limit,
        totalPages: Math.ceil(total / limit),
      },
    };
  }
  // src/tenants/tenants.service.ts
  async updateTenant(id: string, data: UpdateTenantDto) {
    return this.prismaService.tenant.update({
      where: { id },
      data,
    });
  }

  async updateTenantSetting(id: string, settingData: UpdateTenantSettingDto) {
    const existingTenant = await this.prismaService.tenant.findUnique({
      where: { id },
      include: { setting: true },
    });

    if (!existingTenant) {
      throw new Error('Tenant not found');
    }

    if (existingTenant.setting) {
      return this.prismaService.tenant.update({
        where: { id },
        data: {
          setting: {
            update: settingData,
          },
        },
        include: { setting: true },
      });
    } else {
      return this.prismaService.tenant.update({
        where: { id },
        data: {
          setting: {
            create: {
              ...settingData,
            },
          },
        },
        include: { setting: true },
      });
    }
  }
  // get all tenant stats
  async getAllTenantsWithStats() {
    const tenants = await this.prismaService.tenant.findMany({
      include: {
        _count: {
          select: {
            trucks: true,
            Driver: true,
            trips: true,
          },
        },
        trips: {
          where: { status: 'IN_PROGRESS' },
          select: { id: true },
        },
      },
    });

    return tenants.map((t) => ({
      id: t.id,
      name: t.name,
      truckCount: t._count.trucks,
      driverCount: t._count.Driver,
      tripCount: t._count.trips,
      activeTrips: t.trips.length,
    }));
  }

  async delete(id: string) {
    return this.prismaService.tenant.delete({
      where: { id },
    });
  }
}
