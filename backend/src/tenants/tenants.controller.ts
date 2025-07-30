import {
  Controller,
  Post,
  Body,
  Get,
  UseGuards,
  Param,
  Put,
  Query,
  Delete,
} from '@nestjs/common';
import { TenantsService } from './tenants.service';
import { CreateTenantDto } from './dto/create-tenant.dto';
import { JwtAuthGuard } from 'src/auth/security/jwt-auth.guard';
import { RolesGuard } from 'src/auth/security/roles.guard';
import { Roles } from 'src/auth/decorators/roles.decorator';
import {
  UpdateTenantDto,
  UpdateTenantSettingDto,
} from './dto/update-tenant.dto';
import { TenantQueryDTO } from './dto/tenant-query.dto';
import { TenantListResponseDto } from './dto/tenant-response.dto';

@UseGuards(JwtAuthGuard)
@Controller('tenants')
export class TenantsController {
  constructor(private readonly tenantsService: TenantsService) {}

  @UseGuards(RolesGuard)
  @Roles('SUPER_ADMIN')
  @Post()
  async createTenant(@Body() data: CreateTenantDto) {
    return this.tenantsService.createTenant(data);
  }
  @UseGuards(RolesGuard)
  @Roles('SUPER_ADMIN')
  @Get()
  async getAllTenants(
    @Query() query: TenantQueryDTO,
  ): Promise<TenantListResponseDto> {
    return this.tenantsService.getAllTenants(query);
  }
  // get tenant stats
  @UseGuards(RolesGuard)
  @Roles('SUPER_ADMIN')
  @Get('/all')
  async getAllTenantsWithStats() {
    return this.tenantsService.getAllTenantsWithStats();
  }

  @UseGuards(RolesGuard)
  @Roles('ADMIN', 'SUPER_ADMIN')
  @Get(':id')
  async getTenantById(@Param('id') id: string) {
    return this.tenantsService.getTenantById(id);
  }
  @UseGuards(RolesGuard)
  @Roles('ADMIN', 'SUPER_ADMIN')
  @Put(':id')
  async updateTenant(@Param('id') id: string, @Body() data: UpdateTenantDto) {
    return this.tenantsService.updateTenant(id, data);
  }
  @UseGuards(RolesGuard)
  @Roles('ADMIN', 'SUPER_ADMIN')
  @Put(':id/settings')
  async updateTenantSetting(
    @Param('id') id: string,
    @Body() data: UpdateTenantSettingDto,
  ) {
    return this.tenantsService.updateTenantSetting(id, data);
  }
  // delete tenant
  @UseGuards(RolesGuard)
  @Roles('SUPER_ADMIN')
  @Delete(':id')
  async deleteTenant(@Param('id') id: string) {
    const tenant = await this.tenantsService.deleteTenant(id);
    if (!tenant) {
      throw new Error('Tenant delete failed');
    }
    const payload = {
      message: "Deleted tenant and all that tenant's data.",
      data: tenant,
    };
    return payload;
  }
}
