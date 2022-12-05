import {
    Flex,
    Heading,
    ListBox,
    Item,
    ComboBox,
    Checkbox,
} from '@adobe/react-spectrum';
import React, { useContext, useEffect, useState } from 'react';
import { AuctionList } from '../auction-list/AuctionList';
import { CategoriesContext } from '../categories.context';
import { SearchContext } from '../search.context';
import { useHttpQuery } from '../http-query/use-http-query';
import toast from 'react-hot-toast';
import { Toast } from '../toast/Toast';

export const Search = () => {
    const sortOptions = [
        { name: 'Title', uid: 'title' },
        { name: 'Start Date', uid: 'start_date' },
        { name: 'End Date', uid: 'end_date' },
    ];
    const [sort, setSort] = useState(sortOptions[0].uid);
    const [sortDirection, setSortDirection] = useState(false);
    const [categories, setCategories] = useState(new Set([]));
    const [searchParams, setSearchParams] = useState();
    const searchText = useContext(SearchContext);
    const { appResponse, error } = useHttpQuery(
        '/auctions/search',
        searchParams,
    );

    const categoryOptions = useContext(CategoriesContext);

    useEffect(() => {
        if (error) {
            toast.custom((t) => (
                <Toast
                    message={'Failed to load auctions! Try again later'}
                    type="negative"
                    dismissFn={() => toast.remove(t.id)}
                />
            ));
        }
    }, [error]);

    useEffect(() => {
        setSearchParams({
            params: {
                'title:like': searchText,
                'description:like': searchText,
                sortBy: sort,
                sortDescending: sortDirection ? sortDirection : undefined,
                hasCategories:
                    categories.size > 0 ? [...categories].join(',') : undefined,
            },
        });
    }, [searchText, categories, sort, sortDirection]);

    return (
        <Flex
            direction="row"
            alignItems="start"
            justifyContent="start"
            width="100%"
        >
            <Flex direction="column">
                <Heading level="1">Categories</Heading>
                <ListBox
                    selectionMode="multiple"
                    items={categoryOptions.map((opt) => ({ name: opt }))}
                    selectedKeys={categories}
                    onSelectionChange={(selected) =>
                        setCategories(new Set([...selected]))
                    }
                    width="size-2400"
                >
                    {(item) => <Item key={item.name}>{item.name}</Item>}
                </ListBox>
                <Heading level="1">Sort</Heading>
                <ComboBox
                    items={sortOptions}
                    selectedKey={sort}
                    onSelectionChange={setSort}
                    width="size-2400"
                >
                    {(item) => <Item key={item.uid}>{item.name}</Item>}
                </ComboBox>
                <Checkbox
                    isSelected={sortDirection}
                    onChange={setSortDirection}
                >
                    Sort Descending
                </Checkbox>
            </Flex>

            <AuctionList auctions={appResponse?.data || []} />
        </Flex>
    );
};
