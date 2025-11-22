import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Order, Direction } from '../model/page-request';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'table-component',
  imports: [CommonModule, TableModule, ButtonModule],
  templateUrl: './table.component.html',
  styleUrl: './table.component.css',
  providers: [],
})
export class TableComponent implements OnInit {
  @Input('dataSource') dataSource: any[] = [];
  @Input('columnDefinition') columnDefinition: DataSourceColumn[] = [];
  @Input('actions') actions: Action[] = [];

  @Output('sortingEvent') sortingEvent: EventEmitter<Order[]> =
    new EventEmitter();

  columns: string[] = [];

  constructor() {}

  ngOnInit(): void {
    this.columns = this.columnDefinition.map((it) => it.name);

    if (this.actions) {
      this.columns.push('actions');
    }
  }

  sortChange(sortState: any) {
    let ordem: Order[] = [];

    sortState.multisortmeta.forEach((sort: any) => {
      let property = sort.field;
      let direction = sort.order === 1 ? Direction.ASC : Direction.DESC;

      ordem.push({ property, direction });
    });

    this.sortingEvent.emit(ordem);
  }
}

export interface DataSourceColumn {
  name: string;
  label: string;
  getValue: Function;
}

export interface Action {
  icon: string;
  action: Function;
}
