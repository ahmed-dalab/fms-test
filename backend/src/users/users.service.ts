import {
  BadRequestException,
  Injectable,
  NotFoundException,
} from '@nestjs/common';
import { Role, Prisma } from '@prisma/client';
import { RegisterUserDto } from 'src/auth/dto/auth-dto';
import { PrismaService } from 'src/prisma/prisma.service';
import { UsersRepository } from './users.repository';
import { buildUserFilterQuery, isUUID } from 'src/utils/utils';
import { UserNotFoundException } from 'src/auth/exceptions/user-not-found.exception';
import { UserQueryDTO } from './dto/user-query.dto';
import { UserListResponseDTO } from './dto/user-response.dto';

@Injectable()
export class UsersService {
  constructor(private usersRepository: UsersRepository) {}
  // Create a new user
  async createUser(data: RegisterUserDto) {
    const existingUser = await this.usersRepository.getUserByEmail(data.email);
    if (existingUser) {
      throw new BadRequestException(
        `User with email ${data.email} already exists`,
      );
    }
    return this.usersRepository.createUser(data);
  }
  // get user by email
  async findUserByEmail(email: string) {
    return this.usersRepository.getUserByEmail(email);
  }
  // get all users
  async findAllUsers(query: UserQueryDTO): Promise<UserListResponseDTO> {
    const page = query.page ?? 1; // default to 1 if undefined
    const limit = query.limit ?? 10; // default to 10 if undefined
    const skip = (page! - 1) * limit!;

    // filter email and role
    const where = buildUserFilterQuery(query);
    return this.usersRepository.getAllUsers(where, skip, limit, page);
  }
  // get user by id
  async findUserById(id: string) {
    if (!isUUID(id)) {
      throw new BadRequestException(
        'Invalid user ID format. Must be a valid UUID.',
      );
    }

    return this.usersRepository.getUserById(id);
  }
  // get user by reset token
  async findUserByResetToken(resetToken: string) {
    return this.usersRepository.getUserByResetToken(resetToken);
  }
  // Update user details
  async updateUser(id: string, data: Prisma.UserUpdateInput) {
    return this.usersRepository.updateUser(id, data);
  }
  // delete user
  async delete(id) {
    const user = await this.findUserById(id);

    if (!user) {
      throw new NotFoundException(`User with ID ${id} not found`);
    }
    return this.usersRepository.delete(id);
  }
  // get me
  async getMeWithTenant(userId: string, role: string) {
    const user = await this.findUserById(userId);
    if (!user) {
      throw new UserNotFoundException();
    }
    // If SUPER_ADMIN, do not return tenant info
    if (role && role.toUpperCase() === 'SUPER_ADMIN') {
      const { tenant, ...userWithoutTenant } = user;
      return userWithoutTenant;
    }
    return user;
  }
}
