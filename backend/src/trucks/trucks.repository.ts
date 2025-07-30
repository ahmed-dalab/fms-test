import { Injectable } from '@nestjs/common';
import { PrismaService } from 'src/prisma/prisma.service';
import { CreateTruckDto } from './dto/create-truck.dto';
import { UpdateTruckDto } from './dto/update-truck.dto';
import { TruckListResponseDto } from './dto/truck-response.dto';

@Injectable()
export class TrucksRepository {
  constructor(private readonly prismaService: PrismaService) {}
  // This class will handle database operations related to trucks
  // For example, methods to create, read, update, and delete truck records
  async createTruck(data: CreateTruckDto) {
    return this.prismaService.truck.create({
      data,
    });
  }
  // get all trucks
  async getAllTrucks(
    where: any,
    skip: number,
    limit: number,
    page: number,
  ): Promise<TruckListResponseDto> {
    const [data, total] = await this.prismaService.$transaction([
      this.prismaService.truck.findMany({
        where,
        skip,
        take: limit,
        orderBy: { createdAt: 'desc' },
      }),
      this.prismaService.truck.count({ where }),
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
  // get truck by id
  async getTruckById(id: string) {
    return await this.prismaService.truck.findUnique({
      where: { id },
    });
  }
  // update truck by id
  async updateTruckById(id: string, data: UpdateTruckDto) {
    return this.prismaService.truck.update({
      where: { id },
      data,
    });
  }
  // delete truck by id
  async deleteTruckById(id: string) {
    return this.prismaService.truck.delete({
      where: { id },
    });
  }
  async findTruckByVin(vin: string) {
    return this.prismaService.truck.findUnique({ where: { vin } });
  }

  async findTruckByPlateNumber(plateNumber: string) {
    return this.prismaService.truck.findUnique({ where: { plateNumber } });
  }
}
