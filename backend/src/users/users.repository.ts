import { Injectable, NotFoundException } from '@nestjs/common';
import { Prisma, Role } from '@prisma/client';
import { RegisterUserDto } from 'src/auth/dto/auth-dto';
import { PrismaService } from 'src/prisma/prisma.service';
import { UserListResponseDTO } from './dto/user-response.dto';

@Injectable()
export class UsersRepository {
  constructor(private prismaService: PrismaService) {}

  async createUser(data: RegisterUserDto) {
    const user = await this.prismaService.user.create({
      data: {
        name: data.name,
        email: data.email,
        role: data.role?.toUpperCase() as Role,
        password: data.password,
        tenantId: data.tenantId || null, // Set tenantId only if provided
      },
    });
    console.log('user: ', user);
    const { password, ...userWithoutPassword } = user;
    return userWithoutPassword;
  }
  async getUserByEmail(email: string) {
    return this.prismaService.user.findUnique({
      where: { email },
      select: {
        id: true,
        name: true,
        email: true,
        password: true,
        tenantId: true,
        role: true,
        resetToken: true,
        resetTokenExpiry: true,
      },
    });
  }
  async getAllUsers(
    where: any,
    skip: number,
    limit: number,
    page: number,
  ): Promise<UserListResponseDTO> {
    const [data, total] = await this.prismaService.$transaction([
      this.prismaService.user.findMany({
        where,
        skip,
        take: limit,
        orderBy: { createdAt: 'desc' },
        select: {
          id: true,
          name: true,
          email: true,
          role: true,
          tenantId: true,
        },
      }),
      this.prismaService.user.count({ where }),
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
  async getUserById(id: string) {
    const user = await this.prismaService.user.findUnique({
      where: { id },
      select: {
        id: true,
        name: true,
        email: true,
        role: true,
        tenant: {
          select: {
            id: true,
            name: true,
            email: true,
            phoneNumber: true,
          },
        },
      },
    });

    if (!user) {
      throw new NotFoundException(`User with id ${id} not found.`);
    }

    return user;
  }

  async getUserByResetToken(token: string) {
    return await this.prismaService.user.findFirst({
      where: { resetToken: token },
      select: {
        id: true,
        name: true,
        email: true,
        password: true,
        role: true,
        resetToken: true,
        resetTokenExpiry: true,
      },
    });
  }
  async updateUser(id: string, data: Partial<Prisma.UserUpdateInput>) {
    return this.prismaService.user.update({ where: { id }, data });
  }
  async delete(id: string) {
    return await this.prismaService.user.delete({
      where: { id },
    });
  }
}
