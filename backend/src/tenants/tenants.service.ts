import { Injectable } from '@nestjs/common';
import { TenantsRepository } from './tenants.repository';
import { CreateTenantDto } from './dto/create-tenant.dto';
import {
  UpdateTenantDto,
  UpdateTenantSettingDto,
} from './dto/update-tenant.dto';
import { TenantQueryDTO } from './dto/tenant-query.dto';
import { buildTenantFilterQuery } from 'src/utils/utils';
import { TenantListResponseDto } from './dto/tenant-response.dto';

@Injectable()
export class TenantsService {
  constructor(private readonly tenantsRepository: TenantsRepository) {}

  async createTenant(data: CreateTenantDto) {
    // here we have to handle the tenant if the tenant is invalid and throw exception
    return this.tenantsRepository.createTenant(data);
  }
  async getAllTenants(query: TenantQueryDTO): Promise<TenantListResponseDto> {
    const page = query.page ?? 1; // default to 1 if undefined
    const limit = query.limit ?? 10; // default to 10 if undefined
    const skip = (page! - 1) * limit!;

    // filter status and plate number
    const where = buildTenantFilterQuery(query);
    return this.tenantsRepository.getAllTenants(where, skip, limit, page);
  }
  // get tenant by id
  async getTenantById(id: string) {
    return this.tenantsRepository.findTenantById(id);
  }
  // get tenants stats
  async getAllTenantsWithStats() {
    return this.tenantsRepository.getAllTenantsWithStats();
  }
  async updateTenant(id: string, data: UpdateTenantDto) {
    const existingTenant = await this.getTenantById(id);
    if (!existingTenant) {
      throw new Error('Tenant not found');
    }

    return this.tenantsRepository.updateTenant(id, data);
  }

  // tenants.service.ts
  async updateTenantSetting(id: string, settingData: UpdateTenantSettingDto) {
    return this.tenantsRepository.updateTenantSetting(id, settingData);
  }
  async deleteTenant(id: string) {
    return this.tenantsRepository.delete(id);
  }
}
