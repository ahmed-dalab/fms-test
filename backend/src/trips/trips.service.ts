import {
  Injectable,
  InternalServerErrorException,
  NotFoundException,
} from '@nestjs/common';
import { TripsRepository } from './trips.repository';
import { CreateTripDto } from './dto/create-trip.dto';

@Injectable()
export class TripsService {
  constructor(private readonly tripsRepository: TripsRepository) {}

  // create trip
  async createTrip(trip: CreateTripDto) {
    return this.tripsRepository.createTrip(trip);
  }
  // get all trips
  async getAllTrips() {
    return this.tripsRepository.getAllTrips();
  }
  // get single trip by id
  async getTripById(id: string) {
    return this.tripsRepository.getTrip(id);
  }
  // get rev summary
  async getRevenueSummary() {
    return this.tripsRepository.getRevenueSummary();
  }
  // get monthly revenue
  async getMonthlyRevenue() {
    const results = await this.tripsRepository.getMonthlyRevenue();
    const months = [
      '',
      'Jan',
      'Feb',
      'Mar',
      'Apr',
      'May',
      'Jun',
      'Jul',
      'Aug',
      'Sep',
      'Oct',
      'Nov',
      'Dec',
    ];

    return results.map((obj) => ({
      month: months[obj.month],
      revenue: obj.revenue,
    }));
  }

  // get trips count
  async getCount() {
    const length = this.tripsRepository.getTripCount();
    return length;
  }
  // update trip
  async updateTrip(id: string, data) {
    const trip = await this.getTripById(id);
    if (!trip) {
      throw new Error('Trip not found');
    }
    const updatedData = {
      origin: data.origin || trip.origin,
      destination: data.destination || trip.destination,
      status: data.status || trip.status,
      startTime: data.startTime || trip.startTime,
      endTime: data.endTime || trip.endTime,
      truckId: data.truckId || trip.truckId,
      driverId: data.driverId || trip.driverId,
    };
    return await this.tripsRepository.updateTrip(id, updatedData);
  }
  // delete trip

  async deleteTrip(id: string) {
    try {
      const trip = await this.getTripById(id);
      if (!trip) {
        throw new NotFoundException(`Trip with ID ${id} not found`);
      }

      await this.tripsRepository.deleteTrip(id);
      return { message: `Trip with ID ${id} deleted successfully.` };
    } catch (error) {
      console.error(`Error deleting trip with id ${id}:`, error);

      if (error instanceof NotFoundException) {
        throw error; // ✅ Rethrow as-is so NestJS handles it properly
      }

      throw new InternalServerErrorException(
        'An unexpected error occurred while deleting the trip',
      );
    }
  }
}
