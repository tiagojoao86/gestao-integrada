import { CommonModule } from "@angular/common";
import { Component, EventEmitter, Input, Output } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatSelectChange, MatSelectModule } from '@angular/material/select';

@Component({
    selector: 'paginator-component',
    imports: [CommonModule, MatIconModule, MatButtonModule, MatSelectModule],
    templateUrl: './paginator.component.html',
    styleUrl: './paginator.component.css'
})
export class PaginatorComponent {

    @Output('paginationEvent') paginationEvent: EventEmitter<PaginationEvent> = new EventEmitter();

    @Input('pageNumber') pageNumber: number = 1;
    @Input('totalRegisters') totalRegisters: number = 1;
    @Input('itemsPerPage') itemsPerPage = PaginationEvent.DEFAULT_PAGE_SIZE;

    firstPage() {
        if (this.pageNumber === 1) {
            return;
        }
        
        this.pageNumber = 1;
        this.paginationEvent.emit(this.getPaginationEvent());
    }

    previousPage() {
        if (this.pageNumber === 1) {
            return;
        }

        this.pageNumber--;
        this.paginationEvent.emit(this.getPaginationEvent());
    }

    nextPage() {
        if (this.pageNumber === this.getTotalPages()) {
            return;
        }

        this.pageNumber++;
        this.paginationEvent.emit(this.getPaginationEvent());
    }

    lastPage() {
        if (this.pageNumber === this.getTotalPages()) {
            return;
        }

        this.pageNumber = this.getTotalPages();
        this.paginationEvent.emit(this.getPaginationEvent());
    }

    getPaginationEvent(): PaginationEvent {
        return {
            pageNumber: this.pageNumber,
            totalPages: this.getTotalPages(),
            itemsPerPage: this.itemsPerPage,
        };
    }

    changeItemsPerPage(event: MatSelectChange) {
        this.itemsPerPage = event.value;
        this.paginationEvent.emit(this.getPaginationEvent());
    }

    getTotalPages() {
        return Math.ceil(this.totalRegisters / this.itemsPerPage);
    }

}

export class PaginationEvent {

    static readonly DEFAULT_PAGE_SIZE: number = 5;

    pageNumber: number;
    totalPages: number;
    itemsPerPage: number;

    constructor(pageNumber: number, totalPages: number, itemsPerPage: number) {
        this.pageNumber = pageNumber;
        this.totalPages = totalPages;
        this.itemsPerPage = itemsPerPage;
    }
}
