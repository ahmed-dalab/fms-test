import { Injectable, NotFoundException } from '@nestjs/common';
import { DriverRepository } from './driver.repository';
import { CreateDriverDto } from './dto/create-driver.dto';
import { UpdateDriverDto } from './dto/update-driver.dto';
import { RegisterUserDto } from 'src/auth/dto/auth-dto';
import { UsersService } from 'src/users/users.service';
import * as bcrypt from 'bcrypt';
import { Role } from '@prisma/client';
import { PrismaService } from 'src/prisma/prisma.service';
import { QueryDriverDto } from './dto/query-driver.dto';
import { buildDriverFilterQuery } from 'src/utils/utils';
@Injectable()
export class DriverService {
  constructor(
    private readonly driverRepository: DriverRepository,
    private readonly usersService: UsersService,
    private prismaService: PrismaService,
  ) {} //

  // Create a new driver
  async createDriver(
    createDriverDto: CreateDriverDto,
    RegisterUserDto: RegisterUserDto,
    tenantId: string,
  ) {
    try {
      const tenantExists = await this.prismaService.tenant.findUnique({
        where: { id: tenantId },
      });
      if (!tenantExists) {
        throw new NotFoundException(`Tenant with ID ${tenantId} not found`);
      }
      const hashedPassword = await bcrypt.hash(RegisterUserDto.password, 10);
      const userData = {
        ...RegisterUserDto,
        password: hashedPassword,
        role: 'DRIVER' as Role,
        tenantId,
      };
      const newUser = await this.usersService.createUser(userData);
      if (!newUser) {
        throw new Error('Failed to create user for driver');
      }
      return this.driverRepository.create({
        createDriverDto,
        userId: newUser.id,
        tenantId,
      });
    } catch (error) {
      console.error(error); // Log for debugging
      throw error; // ✅ Rethrow the actual exception
    }
  }

  // get all drivers
  async getAllDrivers(query: QueryDriverDto) {
    const page = query.page ?? 1;
    const limit = query.limit ?? 10;
    const skip = (page - 1) * limit;

    const where = buildDriverFilterQuery(query);

    const [data, total] = await Promise.all([
      this.driverRepository.findAll(where, skip, limit),
      this.driverRepository.count(where),
    ]);

    const totalPages = Math.ceil(total / limit);

    return {
      data,
      meta: {
        total,
        page,
        limit,
        totalPages,
      },
    };
  }

  // get driver by id
  async getDriverById(id: string) {
    const driver = await this.driverRepository.findById(id);

    if (!driver) {
      throw new NotFoundException(`Driver with ID ${id} not found`);
    }
    return driver;
  }
  // update driver by id
  async updateDriver(id: string, updateDriverDto: UpdateDriverDto) {
    return this.driverRepository.update(id, updateDriverDto);
  }
  async delete(id: string) {
    const driver = await this.driverRepository.findById(id);
    if (!driver) {
      throw new NotFoundException(`Driver with ID ${id} not found`);
    }

    const user = await this.prismaService.user.findUnique({
      where: { id: driver.userId },
    });
    if (!user) {
      throw new NotFoundException(`User with ID ${driver.userId} not found`);
    }

    // ✅ Delete the driver first
    await this.driverRepository.delete(id);

    // ✅ Then delete the user
    await this.prismaService.user.delete({
      where: { id: user.id },
    });

    return {
      message: `Driver and user with ID ${id} deleted successfully.`,
    };
  }
}
