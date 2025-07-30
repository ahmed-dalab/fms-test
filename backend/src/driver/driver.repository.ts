import { Injectable } from '@nestjs/common';
import { PrismaService } from 'src/prisma/prisma.service';
import { CreateDriverDto } from './dto/create-driver.dto';
import { UpdateDriverDto } from './dto/update-driver.dto';
import { RegisterUserDto } from 'src/auth/dto/auth-dto'; // adjust path if needed
import * as bcrypt from 'bcrypt';
import { Role } from '@prisma/client';

@Injectable()
export class DriverRepository {
  constructor(private readonly prismaService: PrismaService) {}

  // create a new driver with nested user and tenant
  async create({ createDriverDto: CreateDriverDto, userId, tenantId }) {
    if (!tenantId) {
      throw new Error('tenantId is required to create a driver');
    }
    const { licenseNumber, phone, status } = CreateDriverDto;

    const driver = await this.prismaService.driver.create({
      data: {
        licenseNumber,
        phone,
        status,
        tenantId, // just set the foreign key
        userId, // foreign key to the user
      },
      include: {
        user: true,
        tenant: true,
      },
    });
    const {
      user: { password, ...userWithoutPassword },
      ...driverData
    } = driver;
    return {
      driver: driverData,
      user: userWithoutPassword,
    };
  }

  async findAll(where: any, skip: number, take: number) {
    return await this.prismaService.driver.findMany({
      where,
      skip,
      take,
      orderBy: { createdAt: 'desc' },
      select: {
        id: true,
        phone: true,
        licenseNumber: true,
        status: true,
        tenantId: true,
        createdAt: true,
        user: {
          select: {
            id: true,
            name: true,
            email: true,
            role: true,
          },
        },
        trips: {
          select: {
            id: true,
            origin: true,
            destination: true,
            price: true,
            status: true,
            startTime: true,
            endTime: true,
          },
        },
      },
    });
  }

  async count(where: any) {
    return this.prismaService.driver.count({ where });
  }

  // get driver by id
  async findById(id: string) {
    return await this.prismaService.driver.findUnique({
      where: { id },
    });
  }
  // update driver by id
  async update(id: string, updateDriverDto: UpdateDriverDto) {
    return await this.prismaService.driver.update({
      where: { id },
      data: updateDriverDto,
    });
  }
  /// delete
  async delete(id: string) {
    return await this.prismaService.driver.delete({
      where: { id },
    });
  }
}
