import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserAcademicInfo, NewUserAcademicInfo } from '../user-academic-info.model';

export type PartialUpdateUserAcademicInfo = Partial<IUserAcademicInfo> & Pick<IUserAcademicInfo, 'id'>;

export type EntityResponseType = HttpResponse<IUserAcademicInfo>;
export type EntityArrayResponseType = HttpResponse<IUserAcademicInfo[]>;

@Injectable({ providedIn: 'root' })
export class UserAcademicInfoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-academic-infos');

  create(userAcademicInfo: NewUserAcademicInfo): Observable<EntityResponseType> {
    return this.http.post<IUserAcademicInfo>(this.resourceUrl, userAcademicInfo, { observe: 'response' });
  }

  update(userAcademicInfo: IUserAcademicInfo): Observable<EntityResponseType> {
    return this.http.put<IUserAcademicInfo>(
      `${this.resourceUrl}/${this.getUserAcademicInfoIdentifier(userAcademicInfo)}`,
      userAcademicInfo,
      { observe: 'response' },
    );
  }

  partialUpdate(userAcademicInfo: PartialUpdateUserAcademicInfo): Observable<EntityResponseType> {
    return this.http.patch<IUserAcademicInfo>(
      `${this.resourceUrl}/${this.getUserAcademicInfoIdentifier(userAcademicInfo)}`,
      userAcademicInfo,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserAcademicInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserAcademicInfo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserAcademicInfoIdentifier(userAcademicInfo: Pick<IUserAcademicInfo, 'id'>): number {
    return userAcademicInfo.id;
  }

  compareUserAcademicInfo(o1: Pick<IUserAcademicInfo, 'id'> | null, o2: Pick<IUserAcademicInfo, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserAcademicInfoIdentifier(o1) === this.getUserAcademicInfoIdentifier(o2) : o1 === o2;
  }

  addUserAcademicInfoToCollectionIfMissing<Type extends Pick<IUserAcademicInfo, 'id'>>(
    userAcademicInfoCollection: Type[],
    ...userAcademicInfosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userAcademicInfos: Type[] = userAcademicInfosToCheck.filter(isPresent);
    if (userAcademicInfos.length > 0) {
      const userAcademicInfoCollectionIdentifiers = userAcademicInfoCollection.map(userAcademicInfoItem =>
        this.getUserAcademicInfoIdentifier(userAcademicInfoItem),
      );
      const userAcademicInfosToAdd = userAcademicInfos.filter(userAcademicInfoItem => {
        const userAcademicInfoIdentifier = this.getUserAcademicInfoIdentifier(userAcademicInfoItem);
        if (userAcademicInfoCollectionIdentifiers.includes(userAcademicInfoIdentifier)) {
          return false;
        }
        userAcademicInfoCollectionIdentifiers.push(userAcademicInfoIdentifier);
        return true;
      });
      return [...userAcademicInfosToAdd, ...userAcademicInfoCollection];
    }
    return userAcademicInfoCollection;
  }
}
