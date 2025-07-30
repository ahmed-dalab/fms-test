import { Injectable, NotFoundException } from '@nestjs/common';
import { TrucksRepository } from './trucks.repository';
import { CreateTruckDto } from './dto/create-truck.dto';
import { UpdateTruckDto } from './dto/update-truck.dto';
import { Prisma } from '@prisma/client';
import { DuplicateEntryException } from 'src/shared/exceptions/duplicate-exception-entry';
import { QueryTrucksDto } from './dto/query-trucks.dto';
import {
  buildTruckFilterQuery,
  mapPrismaDuplicateFields,
} from 'src/utils/utils';
import { TruckListResponseDto } from './dto/truck-response.dto';

@Injectable()
export class TrucksService {
  constructor(private readonly trucksRepository: TrucksRepository) {}

  // create truck
  async createTruck(data: CreateTruckDto) {
    try {
      return await this.trucksRepository.createTruck(data);
    } catch (error) {
      if (error instanceof Prisma.PrismaClientKnownRequestError) {
        if (error.code === 'P2002') {
          const targetFields = (error.meta?.target as string[]) || [];
          const readableFields = mapPrismaDuplicateFields(targetFields);
          throw new DuplicateEntryException(readableFields);
        }
      }
      throw error; // rethrow other unhandled errors
    }
  }
  // get all trucks
  async getAllTrucks(query: QueryTrucksDto): Promise<TruckListResponseDto> {
    const page = query.page ?? 1; // default to 1 if undefined
    const limit = query.limit ?? 10; // default to 10 if undefined
    const skip = (page! - 1) * limit!;

    // filter status and plate number
    const where = buildTruckFilterQuery(query);
    return this.trucksRepository.getAllTrucks(where, skip, limit, page);
  }
  // get truck by id
  async getTruckById(id: string) {
    const truck = await this.trucksRepository.getTruckById(id);
    if (!truck) {
      throw new NotFoundException(`Truck with ID ${id} not found.`);
    }
    return truck;
  }
  // update truck by id
  async updateTruckById(id: string, data: UpdateTruckDto) {
    const existingTruck = await this.trucksRepository.getTruckById(id);
    if (!existingTruck) {
      throw new NotFoundException(`Truck with ID ${id} not found.`);
    }

    // Check if the VIN is being changed and already exists in another record
    if (data.vin && data.vin !== existingTruck.vin) {
      const vinTaken = await this.trucksRepository.findTruckByVin(data.vin);
      if (vinTaken && vinTaken.id !== id) {
        throw new DuplicateEntryException('VIN');
      }
    }

    // Check if the plateNumber is being changed and already exists in another record
    if (data.plateNumber && data.plateNumber !== existingTruck.plateNumber) {
      const plateTaken = await this.trucksRepository.findTruckByPlateNumber(
        data.plateNumber,
      );
      if (plateTaken && plateTaken.id !== id) {
        throw new DuplicateEntryException('Plate Number');
      }
    }

    return this.trucksRepository.updateTruckById(id, data);
  }

  // delete truck by id
  async deleteTruckById(id: string) {
    try {
      await this.trucksRepository.deleteTruckById(id);
      return { message: `Truck with ID ${id} deleted successfully.` };
    } catch (error) {
      if (error instanceof Prisma.PrismaClientKnownRequestError) {
        if (error.code === 'P2025') {
          throw new NotFoundException(`Truck with ID ${id} was not found.`);
        }
      }
      throw error;
    }
  }
}
