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
import { TrucksService } from './trucks.service';
import { CreateTruckDto } from './dto/create-truck.dto';
import { JwtAuthGuard } from 'src/auth/security/jwt-auth.guard';
import { RolesGuard } from 'src/auth/security/roles.guard';
import { Roles } from 'src/auth/decorators/roles.decorator';
import { UpdateTruckDto } from './dto/update-truck.dto';
import { QueryTrucksDto } from './dto/query-trucks.dto';
import { TruckListResponseDto } from './dto/truck-response.dto';

@UseGuards(JwtAuthGuard)
@Controller('trucks')
export class TrucksController {
  constructor(private readonly truckService: TrucksService) {}

  // create truck
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  @Post()
  async createTruck(@Body() data: CreateTruckDto, @Req() req) {
    const tenantId = req.user.tenantId;
    if (tenantId) {
      data.tenantId = tenantId;
    }
    console.log('data: ', data);
    return this.truckService.createTruck(data);
  }

  // get all trucks

  @Get()
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  async getAllTrucks(
    @Query() query: QueryTrucksDto,
  ): Promise<TruckListResponseDto> {
    return await this.truckService.getAllTrucks(query);
  }
  // get truck by id
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  @Get(':id')
  async getTruckById(@Param('id') id: string) {
    return this.truckService.getTruckById(id);
  }
  // update truck by id
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  @Put(':id')
  async updateTruckById(@Param('id') id: string, @Body() data: UpdateTruckDto) {
    return this.truckService.updateTruckById(id, data);
  }
  // delete truck by id (if needed)
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  @Delete(':id')
  async deleteTruckById(@Param('id') id: string) {
    return this.truckService.deleteTruckById(id);
  }
}
