import { Component, OnInit, ChangeDetectionStrategy, Input } from '@angular/core';

import { ISwaggerEndpointItem } from 'swagger/src/app/models/swagger';

@Component({
  selector: 'app-swagger-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DetailsComponent implements OnInit {
  @Input() data: ISwaggerEndpointItem;

  constructor() {}

  ngOnInit() {}
}
