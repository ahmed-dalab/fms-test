import { Injectable } from '@nestjs/common';
import { PrismaService } from 'src/prisma/prisma.service';
import { CreateTripDto } from './dto/create-trip.dto';
import { Role, TripStatus } from '@prisma/client';

@Injectable()
export class TripsRepository {
  // inject the prisma client
  constructor(private readonly prismaService: PrismaService) {}

  // create trip
  async createTrip(trip: CreateTripDto) {
    return await this.prismaService.trip.create({
      data: {
        origin: trip.origin,
        status: trip.status?.toUpperCase() as TripStatus,
        price: trip.price,
        startTime: trip.startTime || null,
        endTime: trip.endTime || null,
        destination: trip.destination,
        truckId: trip.truckId || null,
        driverId: trip.driverId || null,
        tenantId: trip.tenantId,
      },
    });
  }
  // get all the trips
  async getAllTrips() {
    return await this.prismaService.trip.findMany({
      include: {
        driver: {
          select: {
            id: true,
            phone: true,
            user: {
              select: {
                name: true,
              },
            },
          },
        },
        truck: true,
      },
    });
  }
  // get single trip
  async getTrip(id: string) {
    return await this.prismaService.trip.findUnique({
      where: { id },
      include: {
        driver: true,
        truck: true,
      },
    });
  }
  // get revenue summary
  async getRevenueSummary() {
    return await this.prismaService.$queryRaw`
        SELECT
        SUM(price) AS total_revenue
        FROM
        "Trip" WHERE "status" = 'COMPLETED'
        GROUP BY "tenantId"
        `;
  }
  // get monthly revenue
  async getMonthlyRevenue() {
    // Returns: [{ month: 1, revenue: 1234.56 }, ...]
    return await this.prismaService.$queryRaw<
      Array<{ month: number; revenue: number }>
    >`
    SELECT
      EXTRACT(MONTH FROM "startTime") AS month,
      SUM(price) AS revenue
    FROM "Trip"
    WHERE "status" = 'COMPLETED'
    GROUP BY month
    ORDER BY month
  `;
  }
  // report/trip-count
  async getTripCount() {
    return await this.prismaService.trip.count();
  }
  // update trip
  async updateTrip(id: string, trip) {
    return await this.prismaService.trip.update({
      where: { id },
      data: trip,
    });
  }
  async deleteTrip(id: string) {
    return await this.prismaService.trip.delete({ where: { id } });
  }
}
