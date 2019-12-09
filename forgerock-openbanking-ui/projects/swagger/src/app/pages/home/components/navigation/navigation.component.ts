import { Component, OnInit, ChangeDetectionStrategy, Input } from '@angular/core';

import { ISwaggerNavigation } from 'swagger/src/app/models/swagger';

@Component({
  selector: 'app-swagger-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NavigationComponent implements OnInit {
  @Input() navigation: ISwaggerNavigation;

  constructor() {}

  ngOnInit() {}
}
