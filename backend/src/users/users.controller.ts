import {
  Controller,
  Delete,
  Get,
  HttpCode,
  HttpStatus,
  Param,
  Query,
  UseGuards,
} from '@nestjs/common';
import { UsersService } from './users.service';
import { JwtAuthGuard } from 'src/auth/security/jwt-auth.guard';
import { RolesGuard } from 'src/auth/security/roles.guard';
import { Roles } from 'src/auth/decorators/roles.decorator';
import { CurrentUser } from 'src/auth/decorators/current-user.decorator';
import { UserQueryDTO } from './dto/user-query.dto';

@UseGuards(JwtAuthGuard)
@Controller('users')
export class UsersController {
  constructor(private readonly usersService: UsersService) {}

  // get all users
  @Get()
  @UseGuards(RolesGuard)
  @Roles('SUPER_ADMIN')
  async getAllUsers(@Query() query: UserQueryDTO) {
    return await this.usersService.findAllUsers(query);
  }
  // get me
  @UseGuards(RolesGuard)
  @Roles('ADMIN', 'SUPER_ADMIN')
  @Get('me')
  @UseGuards(JwtAuthGuard)
  @HttpCode(HttpStatus.OK)
  async authMe(@CurrentUser() user: any) {
    // Fetch full user details including tenant info
    return this.usersService.getMeWithTenant(user.userId, user.role);
  }
  // get user by id
  @UseGuards(RolesGuard)
  @Roles('SUPER_ADMIN')
  @Get(':id')
  async getUserById(@Param('id') id: string) {
    return await this.usersService.findUserById(id);
  }
  // delete user
  @Delete(':id')
  @UseGuards(RolesGuard)
  @Roles('SUPER_ADMIN')
  async deleteUser(@Param('id') id: string) {
    return this.usersService.delete(id);
  }
}
