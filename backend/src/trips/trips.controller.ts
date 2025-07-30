import {
  Body,
  Controller,
  Delete,
  Get,
  Param,
  Post,
  Put,
  Req,
  UseGuards,
} from '@nestjs/common';
import { TripsService } from './trips.service';
import { CreateTripDto } from './dto/create-trip.dto';
import { JwtAuthGuard } from 'src/auth/security/jwt-auth.guard';
import { RolesGuard } from 'src/auth/security/roles.guard';
import { Roles } from 'src/auth/decorators/roles.decorator';

@UseGuards(JwtAuthGuard)
@Controller('trips')
export class TripsController {
  constructor(private readonly tripsService: TripsService) {}

  // get all trips
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  @Get()
  async getTrips() {
    return this.tripsService.getAllTrips();
  }
  // create trip
  @Post()
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  async createTrip(@Body() trip: CreateTripDto, @Req() req: any) {
    const tenantId = req.user.tenantId;
    if (tenantId) {
      trip.tenantId = tenantId;
    }
    return this.tripsService.createTrip(trip);
  }
  // get single trip
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  @Get(':id')
  async getTrip(@Param('id') id: string) {
    return this.tripsService.getTripById(id);
  }
  // get revenue summary
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  @Get('report/revenue-summary')
  async getRevenueSummary() {
    return this.tripsService.getRevenueSummary();
  }
  // get monthly revenue
  @Get('report/revenue-by-month')
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  async getMonthlyRevenue() {
    return this.tripsService.getMonthlyRevenue();
  }
  // report/trip-count
  @Get('report/trip-count')
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  async getTripCount() {
    return await this.tripsService.getCount();
  }
  // update trip
  @Put(':id')
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  async updateTrip(@Param('id') id: string, @Body() trip) {
    return this.tripsService.updateTrip(id, trip);
  }
  // delete trip
  @UseGuards(RolesGuard)
  @Roles('ADMIN')
  @Delete(':id')
  async deleteTrip(@Param('id') id: string) {
    return this.tripsService.deleteTrip(id);
  }
}
