import React, { useCallback, useContext, useState } from 'react';
import {
    Flex,
    Grid,
    Text,
    Button,
    TextField,
    ComboBox,
    Item,
    TextArea,
    RangeCalendar,
    LabeledValue,
    NumberField,
} from '@adobe/react-spectrum';
import Image from '@spectrum-icons/workflow/Image';
import { CategoryTagGroup } from '../../category-tag-group/CategoryTagGroup';
import styled from 'styled-components';
import { getLocalTimeZone, today, CalendarDate } from '@internationalized/date';
import { usePostWithToast } from '../../http-query/use-post-with-toast';
import Alert from '@spectrum-icons/workflow/Alert';
import { useNavigate } from 'react-router-dom';
import { CategoriesContext } from '../../categories.context';
import Cancel from '@spectrum-icons/workflow/Cancel';
import { EditState } from './EditState';
import { BidHistory } from './BidHistory';

const getInitialRange = (startDate, endDate) => {
    return {
        start: startDate
            ? new CalendarDate(
                  startDate.getFullYear(),
                  startDate.getMonth() + 1,
                  startDate.getDate(),
              )
            : today(getLocalTimeZone()),
        end: endDate
            ? new CalendarDate(
                  endDate.getFullYear(),
                  endDate.getMonth() + 1,
                  endDate.getDate(),
              )
            : today(getLocalTimeZone()).add({ weeks: 1 }),
    };
};

export const EditAuction = ({ auction, refresh }) => {
    // Set up text field data
    const [image, setImage] = useState(auction?.images?.[0] || '');
    const [title, setTitle] = useState(auction?.title || '');
    const [startingBid, setStartingBid] = useState(auction?.minimumBid || 1);
    const [bidIncrement, setBidIncrement] = useState(
        auction?.bidIncrement || undefined,
    );
    const [description, setDescription] = useState(auction?.description || '');
    const startDate = auction?.startDate
        ? new Date(auction.startDate)
        : undefined;
    const endDate = auction?.endDate ? new Date(auction.endDate) : undefined;
    let [range, setRange] = useState(getInitialRange(startDate, endDate));
    const [categories, setCategories] = useState(
        new Set(auction?.categories || []),
    );
    const [published, setPublished] = useState(auction?.published);
    const [taxPercent, setTaxPercent] = useState(auction?.taxPercent);
    const [error, setError] = useState({});
    const navigate = useNavigate();

    const categoryOptions = useContext(CategoriesContext);

    // Set up a POST to undo our changes
    const undo = usePostWithToast(
        `/auctions/${auction?.id}`,
        {
            ...auction,
            startDate: range.start.toString(),
            endDate: range.end.toString(),
        },
        [auction],
        { message: 'Auction restored!' },
        { message: 'Failed to undo auction save!' },
    );

    // Set up a POST to save our auction state
    const saveAuction = usePostWithToast(
        auction ? `/auctions/${auction.id}` : '/auctions',
        {
            ...auction,
            title,
            image,
            minimumBid: startingBid,
            bidIncrement,
            startDate: range.start.toString(),
            endDate: range.end.toString(),
            categories: [...categories],
            description,
            published,
            taxPercent,
        },
        [
            title,
            image,
            startingBid,
            bidIncrement,
            range,
            categories,
            description,
            published,
            taxPercent,
        ],
        {
            message: auction ? 'Auction updated!' : 'Auction created!',
            actionTitle: auction && 'UNDO',
            actionFn: auction ? () => undo() : undefined,
        },
        { message: 'Failed to save auction!' },
        (appResponse) => {
            if (!auction) {
                navigate(`/auction/${appResponse.data}/edit`);
            } else {
                refresh();
            }
        },
        (axiosError) => setError(axiosError?.response?.data),
    );

    // Set up a POST to copy this auction
    const copyAuction = usePostWithToast(
        `/auctions`,
        {
            ...auction,
            id: undefined,
            startDate: range.start.toString(),
            endDate: range.end.toString(),
        },
        [auction],
        { message: 'Auction Copied' },
        { message: 'Failed to Copy auction' },
        (appResponse) => navigate(`/auction/${appResponse.data}/edit`),
    );

    // Validate our form fields
    const isValidTitle = useCallback(() => title.length > 0, [title]);
    const isValidCategory = useCallback(
        () => categories.size > 0,
        [categories],
    );
    const isValidStartingBid = useCallback(
        () => startingBid > 0,
        [startingBid],
    );
    const isValidBidIncrement = useCallback(
        () => (bidIncrement !== undefined ? bidIncrement > 0 : true),
        [bidIncrement],
    );
    const isValidDescription = useCallback(
        () => description.length > 0,
        [description],
    );
    const isValidStartDate = useCallback(() => {
        const { start } = getInitialRange(startDate, endDate);
        const cleanStart = range.start.compare(start) === 0;
        if (
            !auction ||
            (published && auction?.published === false) ||
            !cleanStart
        ) {
            return range.start.compare(today(getLocalTimeZone())) >= 0;
        } else {
            return true;
        }
    }, [auction, published, range]);
    const isValidTaxPercent = useCallback(
        () => (taxPercent !== undefined ? taxPercent > 0 : true),
        [taxPercent],
    );

    const isValid = () => {
        return (
            isValidTitle() &&
            isValidCategory() &&
            isValidStartingBid() &&
            isValidBidIncrement() &&
            isValidDescription() &&
            isValidStartDate() &&
            isValidTaxPercent()
        );
    };

    const isCleanTitle = useCallback(
        () => title === auction?.title,
        [auction, title],
    );
    const isCleanCategory = useCallback(
        () =>
            auction && auction?.categories.every((cat) => categories.has(cat)),
        [auction, categories],
    );
    const isCleanStartingBid = useCallback(
        () => startingBid === auction?.minimumBid,
        [auction, startingBid],
    );
    const isCleanBidIncrement = useCallback(
        () => bidIncrement === auction?.bidIncrement,
        [auction, bidIncrement],
    );
    const isCleanDescription = useCallback(
        () => description === auction?.description,
        [auction, description],
    );
    const isCleanStart = useCallback(() => {
        const { start } = getInitialRange(startDate, endDate);
        return range.start.compare(start) === 0;
    }, [auction, range]);
    const isCleanEnd = useCallback(() => {
        const { end } = getInitialRange(startDate, endDate);
        return range.end.compare(end) === 0;
    }, [auction, range]);
    const isCleanPublished = useCallback(
        () => published === auction?.published,
        [auction, range],
    );

    const isCleanTaxPercent = useCallback(
        () => taxPercent === auction?.taxPercent,
        [auction, taxPercent],
    );

    // Check if any form fields have been touched
    const isClean = () => {
        return (
            isCleanTitle() &&
            isCleanCategory() &&
            isCleanStartingBid() &&
            isCleanBidIncrement() &&
            isCleanDescription() &&
            isCleanStart() &&
            isCleanEnd() &&
            isCleanPublished() &&
            isCleanTaxPercent()
        );
    };

    // Cancel any ongoing, unsaved changes
    const cancel = useCallback(() => {
        setTitle(auction?.title || '');
        setStartingBid(auction?.minimumBid || 1);
        setBidIncrement(auction?.bidIncrement || undefined);
        setDescription(auction?.description || '');
        setRange(getInitialRange(startDate, endDate));
        setCategories(new Set(auction?.categories || []));
        setPublished(auction?.published);
    }, [
        title,
        categories,
        startingBid,
        bidIncrement,
        description,
        range,
        published,
        auction,
    ]);

    // Is this a terminal state update
    const isTerminal =
        auction?.published === false && auction?.bids?.length > 0;

    return (
        <Grid
            height="calc(100% - 40px)"
            maxWidth="960px"
            areas={[
                'status save',
                'image details',
                'description description',
                'bidhistory bidhistory',
            ]}
            columns={['3fr', '2fr']}
            rows={['auto', 'auto', 'auto', '2fr']}
            columnGap="size-200"
            rowGap="size-150"
        >
            <EditState
                auction={auction}
                published={published}
                setPublished={setPublished}
                saveAuction={saveAuction}
                copyAuction={copyAuction}
                isClean={isClean}
            />
            <Flex
                direction="row"
                space="size-100"
                justifyContent="right"
                gridArea="save"
            >
                <Button
                    variant={isClean() ? 'primary' : 'negative'}
                    onPress={() => (isClean() ? navigate(-1) : cancel())}
                >
                    <Text>{isClean() ? 'Go Back' : 'Cancel'}</Text>
                </Button>
                <Button
                    variant="primary"
                    marginStart="size-100"
                    onPress={() => saveAuction()}
                    isDisabled={!isValid() || isTerminal || isClean()}
                >
                    <Text>Save</Text>
                </Button>
            </Flex>
            <ImageUploadWrapper onClick={() => console.log('click')}>
                <Image size="XXL" />
                <Text>Upload Image</Text>
            </ImageUploadWrapper>
            <Grid
                gridArea="details"
                columns={['2fr', '2fr', '2fr']}
                autoRows="max-content"
                gap="size-100"
                height="100%"
            >
                <TextField
                    label="Auction Title"
                    value={title}
                    onChange={setTitle}
                    width="100%"
                    gridColumnStart="1"
                    gridColumnEnd="4"
                    isDisabled={isTerminal}
                    validationState={
                        isValidTitle()
                            ? isCleanTitle()
                                ? undefined
                                : 'valid'
                            : 'invalid'
                    }
                    errorMessage={!isValidTitle() && 'Title is required'}
                />
                {!auction && (
                    <ComboBox
                        label="Add Category"
                        items={categoryOptions.map((opt) => ({ name: opt }))}
                        onSelectionChange={(selected) =>
                            setCategories(
                                (prev) => new Set([...prev, selected]),
                            )
                        }
                        selectedKey={categories}
                        isDisabled={isTerminal}
                        gridColumnStart="1"
                        gridColumnEnd="4"
                        width="100%"
                    >
                        {(item) => <Item key={item.name}>{item.name}</Item>}
                    </ComboBox>
                )}
                <Flex
                    direction="column"
                    alignItems="start"
                    gap="size-100"
                    height="min-content"
                    width="100%"
                    gridColumnStart="1"
                    gridColumnEnd="4"
                >
                    <LabeledValue label="Categories" />
                    {categories.size ? (
                        <CategoryTagGroup
                            categories={categories}
                            onRemove={
                                auction
                                    ? undefined
                                    : (category) =>
                                          setCategories(
                                              (prev) =>
                                                  new Set(
                                                      Array.from(prev).filter(
                                                          (el) =>
                                                              el !== category,
                                                      ),
                                                  ),
                                          )
                            }
                        />
                    ) : (
                        <Warning>
                            <Alert color="negative" />
                            <Text>Missing Categories</Text>
                        </Warning>
                    )}
                </Flex>
                <Flex
                    direction="column"
                    gridColumnStart="1"
                    gridColumnEnd="4"
                    justifyContent="start"
                    alignItems="start"
                    height="min-content"
                >
                    <LabeledValue
                        label={`Auction Dates: ${range.start.toString()} to ${range.end.toString()}`}
                    />
                    <RangeCalendar
                        alignSelf="center"
                        aria-label="auction dates"
                        value={range}
                        onChange={setRange}
                        isDisabled={isTerminal || auction?.bids?.length > 0}
                        validationState={
                            isValidStartDate()
                                ? isCleanStart() && isCleanEnd()
                                    ? undefined
                                    : 'valid'
                                : 'invalid'
                        }
                        errorMessage={
                            !isValidStartDate() &&
                            'Start date must be on or after today'
                        }
                        minValue={
                            auction ? undefined : today(getLocalTimeZone())
                        }
                    />
                </Flex>

                <NumberField
                    label="Starting Bid"
                    value={startingBid}
                    onChange={setStartingBid}
                    isRequired
                    hideStepper
                    minValue={0}
                    validationState={
                        error?.minimumBid || !isValidStartingBid()
                            ? 'invalid'
                            : isValidStartingBid() && !isCleanStartingBid()
                            ? 'valid'
                            : undefined
                    }
                    errorMessage={
                        !isValidStartingBid()
                            ? 'must be greater than 0'
                            : error?.minimumBid
                    }
                    formatOptions={{
                        style: 'currency',
                        currency: 'USD',
                        currencyDisplay: 'symbol',
                    }}
                    width="100%"
                    isDisabled={isTerminal || auction?.bids?.length > 0}
                />
                <NumberField
                    minValue={0}
                    label="Bid Increment"
                    value={bidIncrement}
                    onChange={setBidIncrement}
                    hideStepper
                    validationState={
                        error?.bidIncrement
                            ? 'invalid'
                            : isValidBidIncrement() && !isCleanBidIncrement()
                            ? 'valid'
                            : undefined
                    }
                    errorMessage={
                        !isValidBidIncrement()
                            ? 'must be greater than 0'
                            : error?.bidIncrement
                    }
                    formatOptions={{
                        style: 'currency',
                        currency: 'USD',
                        currencyDisplay: 'symbol',
                    }}
                    width="100%"
                    isDisabled={isTerminal || auction?.bids?.length > 0}
                />
                <NumberField
                    minValue={0}
                    label="Sales Tax"
                    value={taxPercent}
                    onChange={setTaxPercent}
                    hideStepper
                    step={0.0001}
                    validationState={
                        error?.taxPercent
                            ? 'invalid'
                            : isValidTaxPercent() && !isCleanTaxPercent()
                            ? 'valid'
                            : undefined
                    }
                    errorMessage={
                        !isValidTaxPercent()
                            ? 'must be greater than 0'
                            : error?.taxPercent
                    }
                    formatOptions={{
                        style: 'percent',
                        minimumFractionDigits: 1,
                        maximumFractionDigits: 2,
                        minimumIntegerDigits: 1,
                    }}
                    width="100%"
                    isDisabled={isTerminal}
                />
            </Grid>
            <TextArea
                label="Auction Description"
                gridArea="description"
                width="100%"
                value={description}
                onChange={setDescription}
                isDisabled={isTerminal}
                validationState={
                    isValidDescription()
                        ? isCleanDescription()
                            ? undefined
                            : 'valid'
                        : 'invalid'
                }
                errorMessage={
                    !isValidDescription() && 'Must have a description'
                }
            />
            {!auction || (
                <BidHistory
                    auction={auction}
                    gridArea="bidhistory"
                    width="100%"
                    height="100%"
                />
            )}
        </Grid>
    );
};

const ImageUploadWrapper = styled.div`
    grid-area: image;
    width: 100%;
    height: 100%;
    border-width: 2px;
    border-color: grey;
    border-radius: 5px;
    border-style: solid;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    user-select: none;
`;

const Warning = styled.div`
    display: flex;
    width: 100%;
    align-items: center;
    justify-content: space-around;
    font-size: 90%;
    font-weight: bold;
    color: var(
        --spectrum-button-warning-text-color,
        var(--spectrum-semantic-negative-color-text-small)
    );
`;
