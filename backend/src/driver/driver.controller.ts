import {
  Body,
  Controller,
  Delete,
  Get,
  Param,
  Post,
  Put,
  Query,
  Req,
  UseGuards,
} from '@nestjs/common';
import { DriverService } from './driver.service';
import { RolesGuard } from 'src/auth/security/roles.guard';
import { AuthGuard } from '@nestjs/passport';
import { Roles } from 'src/auth/decorators/roles.decorator';
import { JwtAuthGuard } from 'src/auth/security/jwt-auth.guard';
import { UpdateDriverDto } from './dto/update-driver.dto';
import { CreateDriverDto } from './dto/create-driver.dto';
import { RegisterUserDto } from 'src/auth/dto/auth-dto';
import { QueryDriverDto } from './dto/query-driver.dto';

@UseGuards(JwtAuthGuard)
@Controller('drivers')
export class DriverController {
  constructor(private readonly driverService: DriverService) {}

  // Create a new driver
  @Post()
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  async createDriver(
    @Body('driver') createDriverDto: CreateDriverDto,
    @Body('user') registerUserDto: RegisterUserDto,
    @Req() req: any, // or use a custom decorator for tenant context
  ) {
    const tenantId = req.user.tenantId; // or from your tenant context
    return await this.driverService.createDriver(
      createDriverDto,
      registerUserDto,
      tenantId,
    );
  }

  // get all drivers
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  @Get()
  async getAllDrivers(@Query() query: QueryDriverDto) {
    return this.driverService.getAllDrivers(query);
  }
  // get driver by id
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  @Get(':id')
  async getDriverById(@Param('id') id: string) {
    return this.driverService.getDriverById(id);
  }
  // update driver by id
  @Put(':id')
  async updateDriver(
    @Param('id') id: string,
    @Body() updateDriverDto: UpdateDriverDto,
  ) {
    return this.driverService.updateDriver(id, updateDriverDto);
  }
  // delete driver
  @Delete(':id')
  @UseGuards(RolesGuard)
  @Roles('SUPER_ADMIN', 'ADMIN')
  async delete(@Param('id') id: string) {
    return this.driverService.delete(id);
  }
}
